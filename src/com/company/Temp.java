package com.company;

class Temp extends A implements B{
    int aaa;
    String bbb;
    float ssss;
    Temp(){

    }
    Temp(int a, int b){

    }
    @Override
    public int getAaa(){
        return 1;
    }
    @Override
    public int getBba(){
        return 1;
    }
}

class A{
    public int getAaa(){
        return 1;
    }
    public int getBba(){
        return 1;
    }
}

interface B{

}

class Temp3 extends Temp implements B{
}

class Temp2 extends A implements B{
}

class Temp4 extends Temp implements B{
}