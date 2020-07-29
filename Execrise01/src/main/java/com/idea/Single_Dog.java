package com.idea;

//栈封闭:单例实现线程安全
public class Single_Dog implements Runnable{
    private static final Single_Dog single_dog = new Single_Dog();
    private Single_Dog(){}
    public static Single_Dog getSingle_Dog(){
        if (single_dog==null) throw new NullPointerException();
        System.out.println(single_dog);
        return Single_Dog.single_dog;
    }

    @Override
    public void run() {
        this.getSingle_Dog();
    }
}
class run{
    public static void main(String[] args) {
        int count = 0;
        while (count<20) {
            new Thread(Single_Dog.getSingle_Dog()).start();
            count++;
        }
        System.out.println(count);
    }
}
