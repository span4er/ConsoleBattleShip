package span4er.prod.battleship;

public enum CellType {
    SHIP("\uD83D\uDEA2"),
    HIT_SHIP("💢"),
    BLANK("\uD83C\uDF0A"),
    HIT_BLANK("⭕"),
    DESTROYED_SHIP("\uD83D\uDCA5"),
    OREOL("🟨");

    private final String CODE;

    CellType(String code){
        this.CODE = code;
    }

    public String getCode(){
        return CODE;
    }
}
