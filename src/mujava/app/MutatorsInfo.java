package mujava.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mujava.api.MutationOperator;
import mujava.api.MutantType;

/**
 * This class contains information about all mutant operators currently supported by Api
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 1.1
 */
public class MutatorsInfo {
	private String infoFilePath = "";
	private String infoFileName = "mutantsInfo";
	private static MutatorsInfo instance = null;
	private List<MutationOperator> basicOps;
	private List<MutationOperator> advOps;
	private List<MutationOperator> allOps;
	private TreeMap<MutationOperator, String> shortDescriptions;
	private TreeMap<MutationOperator, String> fullDescriptions;
	private TreeMap<MutationOperator, MutantType> mutantsType;
	private TreeMap<MutationOperator, Boolean> affectsOneLine;
	private TreeMap<MutationOperator, String> developerNotes;
	private Document doc = null;
	
	/**
	 * @return an instance of this class : {@code MutatorsInfo}
	 */
	public static MutatorsInfo getInstance() {
		if (instance == null) {
			instance = new MutatorsInfo();
		}
		return instance;
	}
	
	private MutatorsInfo() {
		reload();
	}
	
	/**
	 * this should be used if the file {@code mutantsInfo.xml} is modified during execution
	 */
	public void reload() {
		clean();
		load();
	}
	
	private void clean() {
		this.basicOps = new LinkedList<MutationOperator>();
		this.advOps = new LinkedList<MutationOperator>();
		this.allOps = new LinkedList<MutationOperator>();
		this.shortDescriptions = new TreeMap<MutationOperator, String>();
		this.fullDescriptions = new TreeMap<MutationOperator, String>();
		this.mutantsType = new TreeMap<MutationOperator, MutantType>();
		this.affectsOneLine = new TreeMap<MutationOperator, Boolean>();
		this.developerNotes = new TreeMap<MutationOperator, String>();
	}
	
	private void load() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			dbf.setNamespaceAware(true);
		    dbf.setValidating(true);
		    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,"UTF-8");
		    db.setErrorHandler(new XMLErrorHandler (new PrintWriter(errorWriter, true)));
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			InputStream xmlDoc = this.getClass().getResourceAsStream(infoFilePath+infoFileName+".xml");
			System.out.println("Parsing : " + this.getClass().getResource(infoFilePath+infoFileName+".xml").toURI() + '\n');
			doc = db.parse(xmlDoc);
			parse(doc);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showInfo(Document d) {
		Node root = d.getFirstChild();
		String date = root.getAttributes().getNamedItem("date").getNodeValue();
		System.out.println("Date : " + date + '\n');
		Node notes = root.getAttributes().getNamedItem("notes");
		System.out.println("Notes : " + (notes==null?"N/A":notes.getNodeValue()) + '\n'+'\n');
		NodeList childs = d.getElementsByTagName("mutant");
		for (int c = 0; c < childs.getLength(); c++) {
			String name = childs.item(c).getAttributes().getNamedItem("name").getNodeValue();
			MutantType mutType = MutantType.valueOf(childs.item(c).getAttributes().getNamedItem("type").getNodeValue());
			String basic = childs.item(c).getAttributes().getNamedItem("isBasic").getNodeValue();
			String shortDescription = childs.item(c).getAttributes().getNamedItem("shortDescription").getNodeValue();
			String fullDescription = childs.item(c).getAttributes().getNamedItem("fullDescription").getNodeValue();
			boolean affectsOneLine = childs.item(c).getAttributes().getNamedItem("affectsOneLine").getNodeValue().compareTo("true") == 0;
			Node developerNotes = childs.item(c).getAttributes().getNamedItem("developerNotes");
			System.out.println("MutationOperator name : " + name);
			System.out.println("MutationOperator type : " + mutType.toString());
			System.out.println((basic.equals("true")?"Basic mutant operator":"Advanced mutant operator"));
			System.out.println("Short description : " + shortDescription + '\n');
			System.out.println("Full description : " + fullDescription + '\n');
			System.out.println("Affects one line : " + affectsOneLine);
			System.out.println("Developer notes : " + (developerNotes==null?"N/A":developerNotes.getNodeValue()) + '\n'+'\n');
		}
	}
	
	/**
	 * Prints all the info related to supported mutation operators
	 */
	public void showInfo() {
		this.showInfo(this.doc);
	}
	
	private void parse(Document d) {
		NodeList childs = d.getElementsByTagName("mutant");
		MutationOperator mutOp = null;
		MutantType mutType = null;
		boolean affectsOneLine;
		boolean basicOp;
		for (int c = 0; c < childs.getLength(); c++) {
			String name = childs.item(c).getAttributes().getNamedItem("name").getNodeValue();
			mutOp = MutationOperator.valueOf(name);
			mutType = MutantType.valueOf(childs.item(c).getAttributes().getNamedItem("type").getNodeValue());
			
			String basic = childs.item(c).getAttributes().getNamedItem("isBasic").getNodeValue();
			basicOp = basic.equals("true");
			
			String shortDescription = childs.item(c).getAttributes().getNamedItem("shortDescription").getNodeValue();
			String fullDescription = childs.item(c).getAttributes().getNamedItem("fullDescription").getNodeValue();
			
			affectsOneLine = childs.item(c).getAttributes().getNamedItem("affectsOneLine").getNodeValue().compareTo("true") == 0;
			
			Node developerNotes = childs.item(c).getAttributes().getNamedItem("developerNotes");
			
			this.mutantsType.put(mutOp, mutType);
			this.affectsOneLine.put(mutOp, affectsOneLine);
			
			if (developerNotes != null) {
				this.developerNotes.put(mutOp, developerNotes.getNodeValue());
			}
			
			if (basicOp) {
				this.basicOps.add(mutOp);
			} else {
				this.advOps.add(mutOp);
			}
			this.allOps.add(mutOp);
			this.shortDescriptions.put(mutOp, shortDescription);
			this.fullDescriptions.put(mutOp, fullDescription);
		}
	}
	
	/**
	 * @return a list will all the basic mutation operators : {@code List<MutationOperator>}
	 */
	public List<MutationOperator> listBasicOperators() {
		return basicOps;
	}
	
	/**
	 * @return a list will all the advanced mutation operators : {@code List<MutationOperator>}
	 */
	public List<MutationOperator> listAdvOperators() {
		return advOps;
	}
	
	/**
	 * @return a list will all the mutation operators : {@code List<MutationOperator>}
	 */
	public List<MutationOperator> allOps() {
		return this.allOps;
	}
	
	/**
	 * Checks if a mutation operator is supported
	 * @param op : the mutation operator to check : {@code MutationOperator}
	 * @return true only if {@code op} is supported : {@code boolean}
	 */
	public boolean isSupported(MutationOperator op) {
		return listBasicOperators().contains(op) || listAdvOperators().contains(op);
	}
	
	/**
	 * Gets a short description for a mutation operator
	 * @param op : the mutation operator : {@code MutationOperator}
	 * @return a short description for {@code op} : {@code String}
	 */
	public String getShortDescription(MutationOperator op) {
		return this.shortDescriptions.get(op);
	}
	
	/**
	 * Gets a detailed description for a mutation operator
	 * @param op : the mutation operator : {@code MutationOperator}
	 * @return a detailed description for {@code op} : {@code String}
	 */
	public String getFullDescription(MutationOperator op) {
		return this.fullDescriptions.get(op);
	}
	
	/**
	 * Gets the mutation operator type
	 * @param op : the mutation operator : {@code MutationOperator}
	 * @return the mutation operator type : {@code MutantType}
	 * @see MutantType
	 */
	public MutantType getMutantType(MutationOperator op) {
		return this.mutantsType.get(op);
	}
	
	/**
	 * @param op : the mutation operator : {@code MutationOperator}
	 * @return {@code true} iff the mutation operator only affects one line : {@code boolean}
	 */
	public boolean affectsOneLine(MutationOperator op) {
		return this.affectsOneLine.get(op);
	}
	
	/**
	 * Gets the developer notes on a mutation operator
	 * @param op : the mutation operator : {@code MutationOperator}
	 * @return the developer notes for {@code op} : {@code String}
	 */
	public String getDeveloperNotes(MutationOperator op) {
		if (this.developerNotes.containsKey(op)) {
			return this.developerNotes.get(op);
		} else {
			return "N/A";
		}
	}

}
