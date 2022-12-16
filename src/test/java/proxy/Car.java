package proxy;

public class Car {
    String name;
    public int doors;
    private final String MAKER = "KIA";

    public Car() {
    }
    public Car(String name, int doors) throws NullPointerException {
    }
    public String getName() {
        return name;
    }
    public int getDoors() {
        return doors;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDoors(int doors) {
        this.doors = doors;
    }
    public void drive(){
        System.out.println("부릉부릉 drive");
    }
    public void stop(){
        System.out.println("끼이익 stop");
    }
    public void fix(String name, int doors) throws NullPointerException{
        this.name = name;
        this.doors = doors;
    }
    public void HI(){
        System.out.println("안녕 나는" + this.name + "이고 문짝은 " + this.doors + "개 있음");
    }
}
