package test.java;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Mutant;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class GenericsWritingTests {

	private Property prop;
	private List<MutantInfo> mutantsInfo;

	public GenericsWritingTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}




	@Parameters
	public static Collection<Object[]> firstValues() {

		//TESTS DEFINITIONS
		List<Pattern> classCA_1ce = new LinkedList<Pattern>();
		classCA_1ce.add(Pattern.compile("public class CA\\_1\\<T\\>"));
		Property propCA_1 = new Property(Mutant.AODS,
										"generics/CA_1",
										"radiatedMethod",
										1,
										1,
										classCA_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_2ce = new LinkedList<Pattern>();
		classCA_2ce.add(Pattern.compile("public class CA\\_2\\<T extends IA\\_NG\\>"));
		Property propCA_2 = new Property(Mutant.AODS,
										"generics/CA_2",
										"radiatedMethod",
										1,
										1,
										classCA_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_3ce = new LinkedList<Pattern>();
		classCA_3ce.add(Pattern.compile("public class CA\\_3\\<T extends IA\\_NG \\& IB\\_NG\\>"));
		Property propCA_3 = new Property(Mutant.AODS,
										"generics/CA_3",
										"radiatedMethod",
										1,
										1,
										classCA_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_4ce = new LinkedList<Pattern>();
		classCA_4ce.add(Pattern.compile("public class CA\\_4\\<T extends Comparable\\<T\\>\\>"));
		Property propCA_4 = new Property(Mutant.AODS,
										"generics/CA_4",
										"radiatedMethod",
										1,
										1,
										classCA_4ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_5ce = new LinkedList<Pattern>();
		classCA_5ce.add(Pattern.compile("public class CA\\_5\\<T extends Comparable\\<\\? extends IA_NG\\>\\>"));
		Property propCA_5 = new Property(Mutant.AODS,
										"generics/CA_5",
										"radiatedMethod",
										1,
										1,
										classCA_5ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_6ce = new LinkedList<Pattern>();
		classCA_6ce.add(Pattern.compile("public class CA\\_6\\<T extends Comparable\\<\\? super IA_NG\\>\\>"));
		Property propCA_6 = new Property(Mutant.AODS,
										"generics/CA_6",
										"radiatedMethod",
										1,
										1,
										classCA_6ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_7ce = new LinkedList<Pattern>();
		classCA_7ce.add(Pattern.compile("public class CA\\_7\\<T extends Comparable\\<\\? super IA_NG\\>\\> implements (.+\\.)?IA\\_NG"));
		Property propCA_7 = new Property(Mutant.AODS,
										"generics/CA_7",
										"radiatedMethod",
										1,
										1,
										classCA_7ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_8ce = new LinkedList<Pattern>();
		classCA_8ce.add(Pattern.compile("public class CA\\_8 implements (.+\\.)?IA\\_G\\<Integer\\>"));
		Property propCA_8 = new Property(Mutant.AODS,
										"generics/CA_8",
										"radiatedMethod",
										1,
										1,
										classCA_8ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_9ce = new LinkedList<Pattern>();
		classCA_9ce.add(Pattern.compile("public class CA\\_9 implements (.+\\.)?IA\\_G\\<Comparable\\<Integer\\>\\>"));
		Property propCA_9 = new Property(Mutant.AODS,
										"generics/CA_9",
										"radiatedMethod",
										1,
										1,
										classCA_9ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_10ce = new LinkedList<Pattern>();
		classCA_10ce.add(Pattern.compile("public class CA\\_10 implements (.+\\.)?IA\\_G\\<Comparable\\<\\? extends List\\<Integer\\>\\>\\>"));
		Property propCA_10 = new Property(Mutant.AODS,
										"generics/CA_10",
										"radiatedMethod",
										1,
										1,
										classCA_10ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_11ce = new LinkedList<Pattern>();
		classCA_11ce.add(Pattern.compile("public class CA\\_11\\<T\\> implements (.+\\.)?IA\\_G\\<Comparable\\<\\? extends List\\<Integer\\>\\>\\>\\, (.+\\.)?IB\\_NG\\, (.+\\.)?IB\\_G\\<T\\>"));
		Property propCA_11 = new Property(Mutant.AODS,
										"generics/CA_11",
										"radiatedMethod",
										1,
										1,
										classCA_11ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_12ce = new LinkedList<Pattern>();
		classCA_12ce.add(Pattern.compile("public class CA\\_12\\<T\\> extends (.+\\.)?AA\\_G\\<Comparable\\<\\? extends T\\>\\> implements (.+\\.)?IA\\_G\\<Comparable\\<\\? extends List\\<Integer\\>\\>\\>\\, (.+\\.)?IB\\_NG\\, (.+\\.)?IB\\_G\\<T\\>"));
		Property propCA_12 = new Property(Mutant.AODS,
										"generics/CA_12",
										"radiatedMethod",
										1,
										1,
										classCA_12ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_IC_1ce = new LinkedList<Pattern>();
		classCA_IC_1ce.add(Pattern.compile("public class (.+\\.)?IC1\\<T\\>"));
		Property propCA_IC_1 = new Property(Mutant.AODS,
										"generics/CA_IC_1",
										"radiatedMethod",
										1,
										1,
										classCA_IC_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_IC_2ce = new LinkedList<Pattern>();
		classCA_IC_2ce.add(Pattern.compile("public class (.+\\.)?IC1\\<T extends IA_NG \\& IB\\_NG\\>"));
		Property propCA_IC_2 = new Property(Mutant.AODS,
										"generics/CA_IC_2",
										"radiatedMethod",
										1,
										1,
										classCA_IC_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_IC_3ce = new LinkedList<Pattern>();
		classCA_IC_3ce.add(Pattern.compile("public class (.+\\.)?IC1\\<T\\> extends (.+\\.)?AA\\_G\\<Comparable\\<\\? extends T\\>\\> implements (.+\\.)?IA\\_G\\<Comparable\\<\\? extends List\\<Integer\\>\\>\\>\\, (.+\\.)?IB\\_NG\\, (.+\\.)?IB\\_G\\<T\\>"));
		Property propCA_IC_3 = new Property(Mutant.AODS,
										"generics/CA_IC_3",
										"radiatedMethod",
										1,
										1,
										classCA_IC_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCA_IC_4ce = new LinkedList<Pattern>();
		classCA_IC_4ce.add(Pattern.compile("public class CA\\_IC\\_4\\<T\\>"));
		classCA_IC_4ce.add(Pattern.compile("public class (.+\\.)?IC1\\<R extends T\\> extends (.+\\.)?AA\\_G\\<Comparable\\<\\? extends T\\>\\> implements (.+\\.)?IA\\_G\\<Comparable\\<\\? extends List\\<Integer\\>\\>\\>\\, (.+\\.)?IB_NG, (.+\\.)?IB\\_G\\<T\\>"));
		Property propCA_IC_4 = new Property(Mutant.AODS,
										"generics/CA_IC_4",
										"radiatedMethod",
										1,
										1,
										classCA_IC_4ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCF_1ce = new LinkedList<Pattern>();
		classCF_1ce.add(Pattern.compile("private (.+\\.)?List\\<Integer\\> atr1\\;"));
		Property propCF_1 = new Property(Mutant.AODS,
										"generics/CF_1",
										"radiatedMethod",
										1,
										1,
										classCF_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCF_2ce = new LinkedList<Pattern>();
		classCF_2ce.add(Pattern.compile("public class CF_2\\<T\\>"));
		classCF_2ce.add(Pattern.compile("private (.+\\.)?List\\<\\? extends T\\> atr1\\;"));
		Property propCF_2 = new Property(Mutant.AODS,
										"generics/CF_2",
										"radiatedMethod",
										1,
										1,
										classCF_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCF_3ce = new LinkedList<Pattern>();
		classCF_3ce.add(Pattern.compile("public class CF\\_3\\<T\\, R extends IA\\_G\\<T\\> \\& IB\\_G\\<T\\>\\>"));
		classCF_3ce.add(Pattern.compile("private (.+\\.)?List\\<\\? super Comparable\\<\\? extends T\\>\\> atr1\\;"));
		Property propCF_3 = new Property(Mutant.AODS,
										"generics/CF_3",
										"radiatedMethod",
										1,
										1,
										classCF_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classFC_1ce = new LinkedList<Pattern>();
		classFC_1ce.add(Pattern.compile("public class FC\\_1\\<T\\>"));
		classFC_1ce.add(Pattern.compile("for \\((.+\\.)?List\\<Integer\\> l \\= new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\; l\\.size\\(\\) \\< 10\\; l\\.add\\( i \\)\\)"));
		Property propFC_1 = new Property(Mutant.AODS,
										"generics/FC_1",
										"radiatedMethod",
										1,
										1,
										classFC_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classFC_2ce = new LinkedList<Pattern>();
		classFC_2ce.add(Pattern.compile("public class FC\\_2\\<T\\>"));
		classFC_2ce.add(Pattern.compile("for \\((.+\\.)?List\\<List\\<\\? super Integer\\>\\> l \\= new (.+\\.)?LinkedList\\<List\\<\\? super Integer\\>\\>\\(\\)\\; l\\.size\\(\\) \\< 10\\; l\\.add\\( new (.+\\.)?LinkedList\\<Integer\\>\\(\\) \\)\\)"));
		Property propFC_2 = new Property(Mutant.AODS,
										"generics/FC_2",
										"radiatedMethod",
										1,
										1,
										classFC_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classFLV_1ce = new LinkedList<Pattern>();
		classFLV_1ce.add(Pattern.compile("(.+\\.)?List\\<Integer\\> lv1 \\= new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\;"));
		Property propFLV_1 = new Property(Mutant.AODS,
										"generics/FLV_1",
										"radiatedMethod",
										1,
										1,
										classFLV_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classFLV_2ce = new LinkedList<Pattern>();
		classFLV_2ce.add(Pattern.compile("(.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> lv1 \\= new (.+\\.)?LinkedList\\<Comparable\\<\\? extends Number\\>\\>\\(\\)\\;"));
		Property propFLV_2 = new Property(Mutant.AODS,
										"generics/FLV_2",
										"radiatedMethod",
										1,
										1,
										classFLV_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classFLV_3ce = new LinkedList<Pattern>();
		classFLV_3ce.add(Pattern.compile("public class FLV\\_3\\<T\\, R\\>"));
		classFLV_3ce.add(Pattern.compile("defMethod\\( (.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> param1 \\)"));
		classFLV_3ce.add(Pattern.compile("(.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> lv1 \\= new (.+\\.)?LinkedList\\<Comparable\\<\\? extends Number\\>\\>\\(\\)\\;"));
		classFLV_3ce.add(Pattern.compile("(.+\\.)?FLV\\_3\\<\\? extends T\\, R\\> lv2 \\= new (.+\\.)?FLV\\_3\\<T\\, R\\>\\(\\)\\;"));
		Property propFLV_3 = new Property(Mutant.AODS,
										"generics/FLV_3",
										"radiatedMethod",
										1,
										1,
										classFLV_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classLV_1ce = new LinkedList<Pattern>();
		classLV_1ce.add(Pattern.compile("(.+\\.)?List\\<Integer\\> atr1\\;"));
		Property propLV_1 = new Property(Mutant.AODS,
										"generics/LV_1",
										"radiatedMethod",
										1,
										1,
										classLV_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classLV_2ce = new LinkedList<Pattern>();
		classLV_2ce.add(Pattern.compile("public class LV\\_2\\<T\\>"));
		classLV_2ce.add(Pattern.compile("(.+\\.)?List\\<\\? extends T\\> atr1\\;"));
		Property propLV_2 = new Property(Mutant.AODS,
										"generics/LV_2",
										"radiatedMethod",
										1,
										1,
										classLV_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classLV_3ce = new LinkedList<Pattern>();
		classLV_3ce.add(Pattern.compile("public class LV\\_3\\<T\\, R extends IA\\_G\\<T\\> \\& IB\\_G\\<T\\>\\>"));
		classLV_3ce.add(Pattern.compile("(.+\\.)?List\\<\\? super Comparable\\<\\? extends Number\\>\\> atr1\\;"));
		Property propLV_3 = new Property(Mutant.AODS,
										"generics/LV_3",
										"radiatedMethod",
										1,
										1,
										classLV_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classLV_4ce = new LinkedList<Pattern>();
		classLV_4ce.add(Pattern.compile("(.+\\.)?List\\<Integer\\> atr1 \\= new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\;"));
		Property propLV_4 = new Property(Mutant.AODS,
										"generics/LV_4",
										"radiatedMethod",
										1,
										1,
										classLV_4ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classLV_5ce = new LinkedList<Pattern>();
		classLV_5ce.add(Pattern.compile("public class LV\\_5\\<T\\, R extends IA\\_G\\<T\\> \\& IB\\_G\\<T\\>\\>"));
		classLV_5ce.add(Pattern.compile("(.+\\.)?List\\<\\? super Comparable\\<\\? extends Number\\>\\> atr1 \\= new (.+\\.)?LinkedList\\<Comparable\\<\\?\\>\\>\\(\\)\\;"));
		classLV_5ce.add(Pattern.compile("(.+\\.)?LV\\_5 atr2 \\= new (.+\\.)?LV\\_5\\(\\)\\;"));
		classLV_5ce.add(Pattern.compile("(.+\\.)?LV\\_5\\<\\? extends T\\, R\\> atr3 \\= new (.+\\.)?LV\\_5\\<T\\, R\\>\\(\\)\\;"));
		Property propLV_5 = new Property(Mutant.AODS,
										"generics/LV_5",
										"radiatedMethod",
										1,
										1,
										classLV_5ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classMS_1ce = new LinkedList<Pattern>();
		classMS_1ce.add(Pattern.compile("public (.+\\.)?List\\<Integer\\> defMethod\\(\\)"));
		Property propMS_1 = new Property(Mutant.AODS,
										"generics/MS_1",
										"radiatedMethod",
										1,
										1,
										classMS_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classMS_2ce = new LinkedList<Pattern>();
		classMS_2ce.add(Pattern.compile("public void defMethod\\( (.+\\.)?List\\<Integer\\> param1 \\)"));
		Property propMS_2 = new Property(Mutant.AODS,
										"generics/MS_2",
										"radiatedMethod",
										1,
										1,
										classMS_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classMS_3ce = new LinkedList<Pattern>();
		classMS_3ce.add(Pattern.compile("public (.+\\.)?List\\<IB\\_G\\<Comparable\\<IB\\_NG\\>\\>\\> defMethod\\( (.+\\.)?List\\<Integer\\> param1 \\)"));
		Property propMS_3 = new Property(Mutant.AODS,
										"generics/MS_3",
										"radiatedMethod",
										1,
										1,
										classMS_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classMS_4ce = new LinkedList<Pattern>();
		classMS_4ce.add(Pattern.compile("public class MS\\_4\\<R\\, S extends IA\\_NG \\& IB\\_G\\<R\\>\\>"));
		classMS_4ce.add(Pattern.compile("public (.+\\.)?MS\\_4\\<\\? extends R\\, S\\> defMethod\\( (.+\\.)?List\\<Integer\\> param1\\, (.+\\.)?MS\\_4\\<R\\, \\? extends S\\> param2 \\)"));
		classMS_4ce.add(Pattern.compile("return new (.+\\.)?MS\\_4\\<R\\, S\\>\\(\\)\\;"));
		Property propMS_4 = new Property(Mutant.AODS,
										"generics/MS_4",
										"radiatedMethod",
										1,
										1,
										classMS_4ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classSW_1ce = new LinkedList<Pattern>();
		classSW_1ce.add(Pattern.compile("public class SW\\_1\\<T\\>"));
		classSW_1ce.add(Pattern.compile("switch \\(\\(new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\)\\.pop\\(\\)\\)"));
		Property propSW_1 = new Property(Mutant.AODS,
										"generics/SW_1",
										"radiatedMethod",
										1,
										1,
										classSW_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classSW_2ce = new LinkedList<Pattern>();
		classSW_2ce.add(Pattern.compile("public class SW\\_2\\<T\\>"));
		classSW_2ce.add(Pattern.compile("switch \\(\\(new (.+\\.)?LinkedList\\<Comparable\\<\\? extends SW\\_2\\<\\? super IB\\_NG\\>\\>\\>\\(\\)\\)\\.size\\(\\)\\)"));
		Property propSW_2 = new Property(Mutant.AODS,
										"generics/SW_2",
										"radiatedMethod",
										1,
										1,
										classSW_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classSW_3ce = new LinkedList<Pattern>();
		classSW_3ce.add(Pattern.compile("public class SW\\_3\\<T extends IA\\_NG \\& IB\\_NG\\>"));
		classSW_3ce.add(Pattern.compile("public static class A\\_G\\<R extends IA\\_NG \\& IB\\_NG\\> extends (.+\\.)?AA\\_G\\<Comparable\\<\\? extends R\\>\\>"));
		classSW_3ce.add(Pattern.compile("switch \\(\\(new generics\\.SW\\_3\\.A\\_G\\<T\\>\\(\\)\\)\\.current\\)"));
		classSW_3ce.add(Pattern.compile("case 1 \\:[ \n]*(.+\\.)?A\\_G\\<\\? extends T\\> a \\= new (.+\\.)?A\\_G\\<T\\>\\(\\)\\;"));
		Property propSW_3 = new Property(Mutant.AODS,
										"generics/SW_3",
										"radiatedMethod",
										1,
										1,
										classSW_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWC_1ce = new LinkedList<Pattern>();
		classWC_1ce.add(Pattern.compile("public class WC\\_1\\<T\\>"));
		classWC_1ce.add(Pattern.compile("while \\(new (.+\\.)?LinkedList\\<Integer\\>\\(\\) \\!\\= null\\)"));
		Property propWC_1 = new Property(Mutant.AODS,
										"generics/WC_1",
										"radiatedMethod",
										1,
										1,
										classWC_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWC_2ce = new LinkedList<Pattern>();
		classWC_2ce.add(Pattern.compile("public class WC\\_2\\<T\\>"));
		classWC_2ce.add(Pattern.compile("while \\(new (.+\\.)?LinkedList\\<Comparable\\<\\? extends Integer\\>\\>\\(\\) \\!\\= null \\&\\& new (.+\\.)?ArrayList\\<Comparable\\<\\? extends Integer\\>\\>\\(\\) \\!\\= null\\)"));
		Property propWC_2 = new Property(Mutant.AODS,
										"generics/WC_2",
										"radiatedMethod",
										1,
										1,
										classWC_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWC_3ce = new LinkedList<Pattern>();
		classWC_3ce.add(Pattern.compile("public class WC\\_3\\<T\\>"));
		classWC_3ce.add(Pattern.compile("public (.+\\.)?Comparable\\<\\? extends T\\> defMethod\\( (.+\\.)?List\\<\\? extends T\\> param1 \\)"));
		classWC_3ce.add(Pattern.compile("while \\(param1 \\!\\= new (.+\\.)?LinkedList\\<T\\>\\(\\)\\)"));
		classWC_3ce.add(Pattern.compile("param1 \\= new (.+\\.)?LinkedList\\<T\\>\\(\\)\\;"));
		Property propWC_3 = new Property(Mutant.AODS,
										"generics/WC_3",
										"radiatedMethod",
										1,
										1,
										classWC_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWLV_1ce = new LinkedList<Pattern>();
		classWLV_1ce.add(Pattern.compile("(.+\\.)?List\\<Integer\\> lv1 \\= new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\;"));
		Property propWLV_1 = new Property(Mutant.AODS,
										"generics/WLV_1",
										"radiatedMethod",
										1,
										1,
										classWLV_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWLV_2ce = new LinkedList<Pattern>();
		classWLV_2ce.add(Pattern.compile("(.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> lv1 \\= new (.+\\.)?LinkedList\\<Comparable\\<\\? extends Number\\>\\>\\(\\)\\;"));
		Property propWLV_2 = new Property(Mutant.AODS,
										"generics/WLV_2",
										"radiatedMethod",
										1,
										1,
										classWLV_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classWLV_3ce = new LinkedList<Pattern>();
		classWLV_3ce.add(Pattern.compile("public class WLV\\_3\\<T\\, R\\>"));
		classWLV_3ce.add(Pattern.compile("public void defMethod\\( (.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> param1 \\)"));
		classWLV_3ce.add(Pattern.compile("(.+\\.)?List\\<Comparable\\<\\? extends Number\\>\\> lv1 \\= new (.+\\.)?LinkedList\\<Comparable\\<\\? extends Number\\>\\>\\(\\)\\;"));
		classWLV_3ce.add(Pattern.compile("(.+\\.)?WLV\\_3\\<\\? extends T\\, R\\> lv2 \\= new (.+\\.)?WLV\\_3\\<T\\, R\\>\\(\\)\\;"));
		Property propWLV_3 = new Property(Mutant.AODS,
										"generics/WLV_3",
										"radiatedMethod",
										1,
										1,
										classWLV_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classIO_1ce = new LinkedList<Pattern>();
		classIO_1ce.add(Pattern.compile("public void defMethod\\( (.+\\.)?List\\<\\?\\> param1 \\)"));
		classIO_1ce.add(Pattern.compile("boolean lv1 \\= param1 instanceof (.+\\.)?LinkedList\\<\\?\\>\\;"));
		Property propIO_1 = new Property(Mutant.AODS,
										"generics/IO_1",
										"radiatedMethod",
										1,
										1,
										classIO_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCAS_1ce = new LinkedList<Pattern>();
		classCAS_1ce.add(Pattern.compile("(.+\\.)?List\\<\\? extends Number\\> lv1 \\= param1 \\? new (.+\\.)?LinkedList\\<Integer\\>\\(\\) \\: new (.+\\.)?LinkedList\\<Float\\>\\(\\)\\;"));
		Property propCAS_1 = new Property(Mutant.AODS,
										"generics/CAS_1",
										"radiatedMethod",
										1,
										1,
										classCAS_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classCAS_2ce = new LinkedList<Pattern>();
		classCAS_2ce.add(Pattern.compile("public class CAS\\_2\\<T\\>"));
		classCAS_2ce.add(Pattern.compile("private (.+\\.)?T atr1\\;"));
		classCAS_2ce.add(Pattern.compile("(.+\\.)?List\\<\\? extends Number\\> lv1 \\= \\(new (.+\\.)?CAS\\_2\\<Comparable\\<\\? extends T\\>\\>\\(\\)\\)\\.atr1 \\!\\= null \\? new (.+\\.)?LinkedList\\<Integer\\>\\(\\) \\: new (.+\\.)?LinkedList\\<Float\\>\\(\\)\\;"));
		Property propCAS_2 = new Property(Mutant.AODS,
										"generics/CAS_2",
										"radiatedMethod",
										1,
										1,
										classCAS_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classR_1ce = new LinkedList<Pattern>();
		classR_1ce.add(Pattern.compile("public (.+\\.)?List\\<\\?\\> defMethod\\(\\)"));
		classR_1ce.add(Pattern.compile("return new (.+\\.)?LinkedList\\<Integer\\>\\(\\)\\;"));
		Property propR_1 = new Property(Mutant.AODS,
										"generics/R_1",
										"radiatedMethod",
										1,
										1,
										classR_1ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classR_2ce = new LinkedList<Pattern>();
		classR_2ce.add(Pattern.compile("public (.+\\.)?List\\<\\?\\> defMethod\\(\\)"));
		classR_2ce.add(Pattern.compile("return new (.+\\.)?LinkedList\\<List\\<Comparable\\<\\? extends Number\\>\\>\\>\\(\\)\\;"));
		Property propR_2 = new Property(Mutant.AODS,
										"generics/R_2",
										"radiatedMethod",
										1,
										1,
										classR_2ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> classR_3ce = new LinkedList<Pattern>();
		classR_3ce.add(Pattern.compile("public class R\\_3\\<R\\, S\\, T\\>"));
		classR_3ce.add(Pattern.compile("public (.+\\.)?R\\_3\\<\\?\\, \\?\\, \\?\\> defMethod\\(\\)"));
		classR_3ce.add(Pattern.compile("return new (.+\\.)?R\\_3\\<Map\\<Comparable\\<\\? extends R\\>\\, \\? extends Comparable\\<T\\>\\>\\, List\\<List\\<Comparable\\<\\? extends S\\>\\>\\>\\, T\\>\\(\\)\\;"));
		Property propR_3 = new Property(Mutant.AODS,
										"generics/R_3",
										"radiatedMethod",
										1,
										1,
										classR_3ce,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfCA_1;
		List<MutantInfo> mfCA_2;
		List<MutantInfo> mfCA_3;
		List<MutantInfo> mfCA_4;
		List<MutantInfo> mfCA_5;
		List<MutantInfo> mfCA_6;
		List<MutantInfo> mfCA_7;
		List<MutantInfo> mfCA_8;
		List<MutantInfo> mfCA_9;
		List<MutantInfo> mfCA_10;
		List<MutantInfo> mfCA_11;
		List<MutantInfo> mfCA_12;
		List<MutantInfo> mfCA_IC_1;
		List<MutantInfo> mfCA_IC_2;
		List<MutantInfo> mfCA_IC_3;
		List<MutantInfo> mfCA_IC_4;
		List<MutantInfo> mfCF_1;
		List<MutantInfo> mfCF_2;
		List<MutantInfo> mfCF_3;
		List<MutantInfo> mfFC_1;
		List<MutantInfo> mfFC_2;
		List<MutantInfo> mfFLV_1;
		List<MutantInfo> mfFLV_2;
		List<MutantInfo> mfFLV_3;
		List<MutantInfo> mfLV_1;
		List<MutantInfo> mfLV_2;
		List<MutantInfo> mfLV_3;
		List<MutantInfo> mfLV_4;
		List<MutantInfo> mfLV_5;
		List<MutantInfo> mfMS_1;
		List<MutantInfo> mfMS_2;
		List<MutantInfo> mfMS_3;
		List<MutantInfo> mfMS_4;
		List<MutantInfo> mfSW_1;
		List<MutantInfo> mfSW_2;
		List<MutantInfo> mfSW_3;
		List<MutantInfo> mfWC_1;
		List<MutantInfo> mfWC_2;
		List<MutantInfo> mfWC_3;
		List<MutantInfo> mfWLV_1;
		List<MutantInfo> mfWLV_2;
		List<MutantInfo> mfWLV_3;
		List<MutantInfo> mfIO_1;
		List<MutantInfo> mfCAS_1;
		List<MutantInfo> mfCAS_2;
		List<MutantInfo> mfR_1;
		List<MutantInfo> mfR_2;
		List<MutantInfo> mfR_3;

		//MUTANTS GENERATION
		mfCA_1 = TestingTools.generateMutants(propCA_1);
		mfCA_2 = TestingTools.generateMutants(propCA_2);
		mfCA_3 = TestingTools.generateMutants(propCA_3);
		mfCA_4 = TestingTools.generateMutants(propCA_4);
		mfCA_5 = TestingTools.generateMutants(propCA_5);
		mfCA_6 = TestingTools.generateMutants(propCA_6);
		mfCA_7 = TestingTools.generateMutants(propCA_7);
		mfCA_8 = TestingTools.generateMutants(propCA_8);
		mfCA_9 = TestingTools.generateMutants(propCA_9);
		mfCA_10 = TestingTools.generateMutants(propCA_10);
		mfCA_11 = TestingTools.generateMutants(propCA_11);
		mfCA_12 = TestingTools.generateMutants(propCA_12);
		mfCA_IC_1 = TestingTools.generateMutants(propCA_IC_1);
		mfCA_IC_2 = TestingTools.generateMutants(propCA_IC_2);
		mfCA_IC_3 = TestingTools.generateMutants(propCA_IC_3);
		mfCA_IC_4 = TestingTools.generateMutants(propCA_IC_4);
		mfCF_1 = TestingTools.generateMutants(propCF_1);
		mfCF_2 = TestingTools.generateMutants(propCF_2);
		mfCF_3 = TestingTools.generateMutants(propCF_3);
		mfFC_1 = TestingTools.generateMutants(propFC_1);
		mfFC_2 = TestingTools.generateMutants(propFC_2);
		mfFLV_1 = TestingTools.generateMutants(propFLV_1);
		mfFLV_2 = TestingTools.generateMutants(propFLV_2);
		mfFLV_3 = TestingTools.generateMutants(propFLV_3);
		mfLV_1 = TestingTools.generateMutants(propLV_1);
		mfLV_2 = TestingTools.generateMutants(propLV_2);
		mfLV_3 = TestingTools.generateMutants(propLV_3);
		mfLV_4 = TestingTools.generateMutants(propLV_4);
		mfLV_5 = TestingTools.generateMutants(propLV_5);
		mfMS_1 = TestingTools.generateMutants(propMS_1);
		mfMS_2 = TestingTools.generateMutants(propMS_2);
		mfMS_3 = TestingTools.generateMutants(propMS_3);
		mfMS_4 = TestingTools.generateMutants(propMS_4);
		mfSW_1 = TestingTools.generateMutants(propSW_1);
		mfSW_2 = TestingTools.generateMutants(propSW_2);
		mfSW_3 = TestingTools.generateMutants(propSW_3);
		mfWC_1 = TestingTools.generateMutants(propWC_1);
		mfWC_2 = TestingTools.generateMutants(propWC_2);
		mfWC_3 = TestingTools.generateMutants(propWC_3);
		mfWLV_1 = TestingTools.generateMutants(propWLV_1);
		mfWLV_2 = TestingTools.generateMutants(propWLV_2);
		mfWLV_3 = TestingTools.generateMutants(propWLV_3);
		mfIO_1 = TestingTools.generateMutants(propIO_1);
		mfCAS_1 = TestingTools.generateMutants(propCAS_1);
		mfCAS_2 = TestingTools.generateMutants(propCAS_2);
		mfR_1 = TestingTools.generateMutants(propR_1);
		mfR_2 = TestingTools.generateMutants(propR_2);
		mfR_3 = TestingTools.generateMutants(propR_3);

		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propCA_1, mfCA_1},			//00
				{propCA_2, mfCA_2},			//01
				{propCA_3, mfCA_3},			//02
				{propCA_4, mfCA_4},			//03
				{propCA_5, mfCA_5},			//04
				{propCA_6, mfCA_6},			//05
				{propCA_7, mfCA_7},			//06
				{propCA_8, mfCA_8},			//07
				{propCA_9, mfCA_9},			//08
				{propCA_10, mfCA_10},		//09
				{propCA_11, mfCA_11},		//010
				{propCA_12, mfCA_12},		//011
				{propCA_IC_1, mfCA_IC_1},	//012
				{propCA_IC_2, mfCA_IC_2},	//013
				{propCA_IC_3, mfCA_IC_3},	//014
				{propCA_IC_4, mfCA_IC_4},	//015
				{propCF_1, mfCF_1},			//016
				{propCF_2, mfCF_2},			//017
				{propCF_3, mfCF_3},			//018
				{propFC_1, mfFC_1},			//020
				{propFC_2, mfFC_2},			//021
				{propFLV_1, mfFLV_1},		//022
				{propFLV_2, mfFLV_2},		//023
				{propFLV_3, mfFLV_3},		//024
				{propLV_1, mfLV_1},			//025
				{propLV_2, mfLV_2},			//026
				{propLV_3, mfLV_3},			//027
				{propLV_4, mfLV_4},			//028
				{propLV_5, mfLV_5},			//029
				{propMS_1, mfMS_1},			//030
				{propMS_2, mfMS_2},			//031
				{propMS_3, mfMS_3},			//032
				{propMS_4, mfMS_4},			//033
				{propSW_1, mfSW_1},			//034
				{propSW_2, mfSW_2},			//035
				{propSW_3, mfSW_3},			//036
				{propWC_1, mfWC_1},			//037
				{propWC_2, mfWC_2},			//038
				{propWC_3, mfWC_3},			//039
				{propWLV_1, mfWLV_1},		//040
				{propWLV_2, mfWLV_2},		//041
				{propWLV_3, mfWLV_3},		//042
				{propIO_1, mfIO_1},			//043
				{propCAS_1, mfCAS_1},		//044
				{propCAS_2, mfCAS_2},		//045
				{propR_1, mfR_1},			//046
				{propR_2, mfR_2},			//047
				{propR_3, mfR_3},			//048
		});
	}
	
	@Test
	public void testCorrectMutantsGenerated() {
		assertTrue(TestingTools.testExpectedMutantsFound(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testThatMutantsCompile() {
		assertTrue(TestingTools.testThatMutantsCompile(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testCorrectNumberOfMutants() {
		assertTrue(TestingTools.testCorrectNumberOfMutants(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testMutantsMD5hash() {
		assertTrue(TestingTools.testMD5Hash(this.mutantsInfo));
	}

}
