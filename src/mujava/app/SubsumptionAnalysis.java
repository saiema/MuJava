package mujava.app;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import mujava.api.MutationOperator;
import static mujava.api.MutationOperator.*;

public class SubsumptionAnalysis {
	
	public static boolean fullPrint = false;
	private List<SubsumptionNode> nodes = new LinkedList<>();
	private List<SubsumptionNode> dominatorNodes = null;
	private List<SubsumptionNode> pureDominatorNodes = null;
	private boolean dataChanged = false;
	private static enum AnalisisType {EQUIVALENT, SUBSUMING};
	private boolean analyzed = false;
	
	public synchronized void add(SubsumptionNode snode) {
		for (SubsumptionNode o : nodes) {
			if (o == snode) throw new IllegalArgumentException("mujava.app.SubsumptionAnalysis#add(SubsumptionNode) : repeated node");
			if (o.getMutantsID().compareTo(snode.getMutantsID()) == 0) {
				o.merge(snode);
				return;
			}
		}
		nodes.add(snode);
		dataChanged = true;
	}
	
	public void analyse() {
		if (analyzed) return;
		analize(AnalisisType.EQUIVALENT);
		analize(AnalisisType.SUBSUMING);
		analyzed = true;
	}
	
	private void analize(AnalisisType at) {
		boolean merged = false;
		do {
			merged = false;
			for (int i = 0; i < nodes.size(); i++) {
				SubsumptionNode node = nodes.get(i);
				for (int j = (i+1); j < nodes.size(); j++) {
					SubsumptionNode onode = nodes.get(j);
					switch (at) {
						case EQUIVALENT: {
							if (node.mergeIfEquivalent(onode)) {
								merged = true;
								nodes.remove(j);
							}
							break;
						}
						case SUBSUMING: {
							if (node.subsume(onode)) {
								node.addSubsumedNode(onode);
								onode.addSubsumingdNode(node);
							} else if (onode.subsume(node)) {
								node.addSubsumingdNode(onode);
								onode.addSubsumedNode(node);
							}
							break;
						}
					}
					
				}
				if (merged) break;
			}
		} while (merged);
	}
	
	public List<SubsumptionNode> getDominatorNodes() {
		analyse();
		List<SubsumptionNode> dominators;
		if (!dataChanged && dominatorNodes != null) {
			return dominatorNodes;
		} else if (dataChanged || dominatorNodes == null) {
			dominators = new LinkedList<>();
			for (SubsumptionNode n : nodes) {
				if (n.isDominator()) dominators.add(n);
			}
			dominatorNodes = dominators;
		}
		dataChanged = false;
		return dominatorNodes;
	}
	
	public List<SubsumptionNode> getDominatorNodes(boolean includePure) {
		analyse();
		List<SubsumptionNode> dominators = new LinkedList<>();
		for (SubsumptionNode n : getDominatorNodes()) {
			if (!includePure && n.isPure()) continue;
			dominators.add(n);
		}
		return dominators;
	}
	
	public List<SubsumptionNode> getPureDominatorNodes() {
		analyse();
		List<SubsumptionNode> pureDominators;
		if (!dataChanged && pureDominatorNodes != null) {
			return pureDominatorNodes;
		} else if (dataChanged || pureDominatorNodes == null) {
			pureDominators = new LinkedList<>();
			for (SubsumptionNode n : nodes) {
				if (n.isDominator() && n.isPure()) pureDominators.add(n);
			}
			pureDominatorNodes = pureDominators;
		}
		dataChanged = false;
		return pureDominatorNodes;
	}
	
	public List<SubsumptionNode> getNonDominatorNodes(boolean includeLeafs) {
		analyse();
		List<SubsumptionNode> nondominators = new LinkedList<>();
		for (SubsumptionNode n : nodes) {
			if (!n.isDominator()) {
				if (n.isLeaf() && !includeLeafs) continue;
				nondominators.add(n);
			}
		}
		return nondominators;
	}
	
	public List<SubsumptionNode> getLeafNodes() {
		analyse();
		List<SubsumptionNode> leafs = new LinkedList<>();
		for (SubsumptionNode n : nodes) {
			if (n.isLeaf()) leafs.add(n);
		}
		return leafs;
	}
	
	@Override
	public String toString() {
		analyse();
		StringBuilder sb = new StringBuilder("");
		Collections.sort(nodes);
		sb.append("Dynamic Subsumption Analysis\n");
		SubsumptionNode.fullPrint = fullPrint;
		Iterator<SubsumptionNode> it = nodes.iterator();
		while (it.hasNext()) {
			sb.append(it.next().toString());
			if (it.hasNext()) sb.append("\n-------\n");
		}
		return sb.toString();
	}
	
	public boolean generateDotFile(String file) {
		analyse();
		Path pfile = Paths.get(file);
		if (Files.exists(pfile)) {
			System.err.println(pfile.toString() + " already exists");
			return true;
		}
		Path parent = pfile.toAbsolutePath().getParent();
		try {
			Files.createDirectories(parent);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		try {
			pfile = Files.createFile(pfile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		analyse();
		String indent = "\t\t";
		StringBuilder sb = new StringBuilder();
		sb.append("digraph dynamic_subsumption_graph {\n");
		sb.append(indent).append("rankdir=TD;\n");
		sb.append(indent).append("page=\"15,15\"\n");
		sb.append(indent).append("ratio=\"auto\";\n");
		sb.append(indent).append("ranksep=\"1.5 equally\";\n");
		if (!getPureDominatorNodes().isEmpty()) {
			sb.append(indent).append("node [shape = ellipse fontsize = 30 style=filled fillcolor = gray];");
			Iterator<SubsumptionNode> it = getPureDominatorNodes().iterator();
			boolean pureDominatorsFound = false;
			while (it.hasNext()) {
				sb.append(it.next().shortNodeName());
				if (it.hasNext()) sb.append(" ");
				pureDominatorsFound = true;
			}
			if (pureDominatorsFound) sb.append(";").append("\n");
		}
		sb.append(indent).append("node [shape = ellipse fontsize = 30 style=solid fillcolor = none];");
		Iterator<SubsumptionNode> it = getDominatorNodes(false).iterator();
		boolean dominatorsFound = false;
		while (it.hasNext()) {
			sb.append(it.next().shortNodeName());
			if (it.hasNext()) sb.append(" ");
			dominatorsFound = true;
		}
		if (dominatorsFound) sb.append(";");
		sb.append("\n");
		sb.append(indent).append("node [shape = ellipse fontsize = 30 style=dashed fillcolor = none];\n");
		Iterator<SubsumptionNode> nodomnoleafIterator = getNonDominatorNodes(false).iterator();
		boolean nonleafsfound = false;
		while (nodomnoleafIterator.hasNext()) {
			SubsumptionNode node = nodomnoleafIterator.next();
			if (node.isEquivalentToOriginal()) continue;
			sb.append(node.shortNodeName());
			if (nodomnoleafIterator.hasNext()) sb.append(" ");
			nonleafsfound = true;
		}
		if (nonleafsfound) sb.append(";");
		sb.append("\n");
		sb.append(indent).append("node [shape = ellipse fontsize = 30 style=dotted fillcolor = none];\n");
		Iterator<SubsumptionNode> leafsIt = getLeafNodes().iterator();
		boolean leafsFound = false;
		while (leafsIt.hasNext()) {
			sb.append(leafsIt.next().shortNodeName());
			if (leafsIt.hasNext()) sb.append(" ");
			leafsFound = true;
		}
		if (leafsFound) sb.append(";");
		sb.append("\n");
		it = nodes.iterator();
		while (it.hasNext()) {
			SubsumptionNode n = it.next();
			if (n.isEquivalentToOriginal() || (n.getSubsumedNodes().isEmpty() && n.getSubsumingNodes().isEmpty())) continue;
			Path nref = saveNodeToFile(parent, n);
			if (nref == null) return false;
			sb.append(indent)
				.append(n.shortNodeName())
				.append("[").append("URL=\"file:").append(nref.toString()).append("\"];\n");
			if (!n.getSubsumedNodes().isEmpty()) {
				Iterator<SubsumptionNode> itsubsumed = n.getSubsumedNodes().iterator();
				while (itsubsumed.hasNext()) {
					SubsumptionNode nsub = itsubsumed.next();
					sb.append(indent)
						.append(n.shortNodeName())
							.append(" -> ")
						.append(nsub.shortNodeName())
					.append(";\n");
				}
			}
		}
		sb.append("}\n");
		try {
			Files.write(pfile, sb.toString().getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean generateSVG(String file, boolean reduceDynamicSubsumptionGraph) {
		Path pfile = Paths.get(file.replace(".dot", ".svg"));
		if (Files.exists(pfile)) {
			System.err.println(pfile.toString() + " already exists");
			return true;
		}
		//dot -Tsvg -odsg.svg dsg.dot
		String[] args = reduceDynamicSubsumptionGraph?getReducedGraphGenerationCommand(file):getGraphGenerationCommand(file);
		ProcessBuilder pb = new ProcessBuilder(args);
		File errorLog = new File("error.log");
		pb.redirectError(Redirect.appendTo(errorLog));
		Process p;
		try {
			p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode != 0) {
				System.err.println("Failed to generate svg file");
				return false;
			} else {
				return true;
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private String[] getGraphGenerationCommand(String file) {
		String[] args = new String[4];
		args[0] = "dot";
		args[1] = "-Tsvg";
		args[2] = "-o"+file.replace(".dot", ".svg");
		args[3] = file;
		return args;
	}
	
	private String[] getReducedGraphGenerationCommand(String file) {
		//String[] cmd = { "/bin/sh", "-c", "ps -ef | grep export" };
		//tred test.dot | dot -T png > test.png
		String[] args = new String[3];
		args[0] = "/bin/sh";
		args[1] = "-c";
		args[2] = "tred " + file + " | dot -Tsvg -o " + file.replace(".dot", ".svg");
		return args;
	}
	
	private Path saveNodeToFile(Path parent, SubsumptionNode n) {
		String nodeName = n.shortNodeName();
		String fileName = nodeName + ".txt";
		Path pfile = Paths.get(parent.toString(), fileName);
		if (Files.exists(pfile)) {
			return pfile.getFileName();
		}
		try {
			pfile = Files.createFile(pfile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			boolean fullPrintBackup = SubsumptionNode.fullPrint;
			SubsumptionNode.fullPrint = true;
			String data = n.toString();
			SubsumptionNode.fullPrint = fullPrintBackup;
			Files.write(pfile, data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return pfile.getFileName();
	}
	
	public Map<String, Integer> getDominatorMutantsPerOperator() {
		analyse();
		Map<String, Integer> res = new TreeMap<>();
		for (SubsumptionNode d : getDominatorNodes()) {
			Set<MutationOperator> alreadySeen = new TreeSet<>(new Comparator<MutationOperator>() {

				@Override
				public int compare(MutationOperator o1, MutationOperator o2) {
					if (isPRVO(o1) && isPRVO(o2)) return 0;
					return o1.compareTo(o2);
				}
				
			});
			for (MutantInfo m : d.getMutants()) {
				MutationOperator op = m.getOpUsed();
				String opName = isPRVO(op)?"PRVO":op.toString();
				if (alreadySeen.add(op)) {
					if (res.containsKey(opName)) {
						Integer cv = res.get(opName);
						res.put(opName, cv + 1);
					} else {
						res.put(opName, 1);
					}
				}
			}
		}
		return res;
	}
	
	public Map<String, Integer> getPureDominatorMutantsPerOperator() {
		analyse();
		Map<String, Integer> res = new TreeMap<>();
		for (SubsumptionNode d : getDominatorNodes()) {
			if (!d.isPure()) continue;
			MutationOperator op = d.getMutants().get(0).getOpUsed();
			String opName = isPRVO(op)?"PRVO":op.toString();
			if (res.containsKey(opName)) {
				Integer cv = res.get(opName);
				res.put(opName, cv + 1);
			} else {
				res.put(opName, 1);
			}
		}
		return res;
	}
	
	public boolean writeDominatorMutantsPerOperator(String file) {
		Path pfile = Paths.get(file);
//		int totalDomMutants = 0;
//		int totalDomPRVOMutants = 0;
		if (Files.exists(pfile)) {
			System.err.println(pfile.toString() + " already exists");
			return true;
		}
		int dominatorNodes = getDominatorNodes().size();
		StringBuilder sb = new StringBuilder();
		sb.append("Total dominator nodes\t:\t").append(dominatorNodes).append("\n");
		Map<String, Integer> pureDominators = getPureDominatorMutantsPerOperator();
		for (Entry<String, Integer> opData : getDominatorMutantsPerOperator().entrySet()) {
			float dominationRation = ((float)opData.getValue()/(float)dominatorNodes)*100.0f;
			Integer opDominatorNodes = opData.getValue();
			Integer opPureDominatorNodes = pureDominators.containsKey(opData.getKey())?pureDominators.get(opData.getKey()):0;
			float purenessRatio = ((float)opPureDominatorNodes/(float)opDominatorNodes)*100.0f;
//			if (isPRVO(opData.getKey())) totalDomPRVOMutants += opData.getValue();
//			totalDomMutants += opData.getValue();
			sb.append(opData.getKey())
			.append("\t[Dominator nodes involved: ").append(opData.getValue()).append("]")
			.append("\t[Domination ratio: ").append(dominationRation).append("%]")
			.append("\t[Pureness ratio: ").append(purenessRatio).append("%]")
			.append("\n");
		}
//		sb.append("Total dominator mutants\t\t:\t").append(totalDomMutants).append("\n");
//		sb.append("Total PRVO dominator mutants\t:\t").append(totalDomPRVOMutants).append("\n");
		try {
			Files.write(pfile, sb.toString().getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	private boolean isPRVO(MutationOperator op) {
//		switch (op) {
//			case PRVOL:
//			case PRVOL_SMART:
//			case PRVOR:
//			case PRVOR_REFINED:
//			case PRVOR_SMART:
//			case PRVOU:
//			case PRVOU_REFINED:
//			case PRVOU_SMART: return true;
//			default: return false;
//		}
//	}

}
