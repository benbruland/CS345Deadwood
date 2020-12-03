//Authors: Benjamin Bruland, Lukas McIntosh
package model;

public class GuiData {
    private int height;
    private int width;
    private int x;
    private int y;

    public GuiData() {

    }

    public GuiData(int h, int w, int coordX, int coordY) {
        this.height = h;
        this.width = w;
        this.x = coordX;
        this.y = coordY;
    }

    public int getX() {
        return this.x;
    }

    public int getY() { 
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setX(int coordX) {
        this.x = coordX;
    }

    public void setY(int coordY) {
        this.y = coordY;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public void setWidth(int w) {
        this.width = w;
    }
    
    public void printGuiData() {
        System.out.println("Height = " + this.height);
        System.out.println("Width = "   + this.width);
        System.out.printf("Coordinates: (%d,%d)\n" , this.x, this.y);
    }
}