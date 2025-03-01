package ru.bsuedu.cad.lab;

public class Persona {


    private Long id;  // Уникальный ID персоны
    private String name;  // Имя персоны
    private String arcana;  // Аркана персоны
    private int level;  // Уровень персоны (1-99)
    private int strength;  // Сила (1-99)
    private int magic;  // Магия (1-99)
    private int endurance;  // Выносливость (1-99)
    private int agility;  // Ловкость (1-99)
    private int luck;  // Удача (1-99)
    private Long characterId;

    // Конструктор
    public Persona() {
    }

    public Persona(Long id, String name, String arcana, int level, int strength, int magic, int endurance, int agility, int luck, long characterId) {
        this.id = id;
        this.name = name;
        this.arcana = arcana;
        this.level = level;
        this.strength = strength;
        this.magic = magic;
        this.endurance = endurance;
        this.agility = agility;
        this.luck = luck;
        this.characterId = characterId;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArcana() {
        return arcana;
    }

    public void setArcana(String arcana) {
        this.arcana = arcana;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }


    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    // Переопределение метода toString
    @Override
    public String toString() {
        return "Persona{id=" + id +
                ", name='" + name + '\'' +
                ", arcana='" + arcana + '\'' +
                ", level=" + level +
                ", strength=" + strength +
                ", magic=" + magic +
                ", endurance=" + endurance +
                ", agility=" + agility +
                ", luck=" + luck  +
                ", characterId=" + characterId +
                '}';
    }
}