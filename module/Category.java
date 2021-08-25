package Notorious.module;

public enum Category {

	COMBAT("Combat"), RENDER("Render"), PLAYER("Player"), MOVEMENT("Movement"), MISC("Misc"), EXPLOIT("Exploit");

	public String name;
	public int moduleIndex;

	Category(String name) {
		this.name = name;
	}

}
