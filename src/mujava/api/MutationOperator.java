package mujava.api;

public enum MutationOperator {
	BEE,
	CRCR,
	EAM,
	EMM,
	EOA, EOA_DUMB, EOA_STRICT,
	EOC, EOC_SMART,
	AMC,
	IHD,
	IHI,
	IOD,
	IOP,
	IPC,
	ISD, ISD_SMART,
	ISI, ISI_SMART,
	JTD,
	JDC,
	JID,
	JSD,
	JSI,
	JTI, JTI_SMART,
	OAN, OAN_RELAXED,
	OMR,
	PCC,
	PCD,
	PCI,
	PMD,
	PNC,
	PPD,
	PRVO,
	PRVOL, PRVOL_SMART,
	PRVOR, PRVOR_REFINED, PRVOR_SMART,
	PRVOU, PRVOU_REFINED, PRVOU_SMART,
	AODS,
	AODU,
	AOIS,
	AOIU,
	AORB,
	AORS,
	AORU,
	ASRS,
	COD,
	COI,
	COR,
	LOD,
	LOI,
	LOR,
	ROR,
	SOR,
	NPER,
	MULTI, //used for mutants generated with several mutations
	NONE;  //used when no operator has been used (no mutation applied)
	
		public static boolean isPRVO(MutationOperator op) {
			switch (op) {
			case PRVOL:
			case PRVOL_SMART:
			case PRVOR:
			case PRVOR_REFINED:
			case PRVOR_SMART:
			case PRVOU:
			case PRVOU_REFINED:
			case PRVOU_SMART: return true;
			default: return false;
		}
	}
	
}
