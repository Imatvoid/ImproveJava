### sychronized同步方法



#### 锁的对象

- 对象（类实例）

- 类本身



#### 即使这个对象/类被锁住，仍可以调用非synchronized方法



#### 支持重入



#### 出现异常自动释放



#### 同步的方法不具有继承性

子类如果继承，需要手动添加synchronized方法





### synchronized同步语句块（推荐）



#### 锁的对象

- 对象（类实例）
- 类本身

synchronized(this)

synchronized(xxx.class)



类锁与对象锁互不影响，锁住的是不同对象

````java
public class SynTestThread implements Runnable {


    public synchronized static  void printA(){


          try {
              System.out.println(Thread.currentThread().getName()+" in  printA");
              Thread.sleep(3000);
              System.out.println(Thread.currentThread().getName()+" out printA");
          }catch (InterruptedException e ){

          }

    }

    public synchronized   void printB(){
        try {
            System.out.println(Thread.currentThread().getName()+" in printB");

            System.out.println(Thread.currentThread().getName()+" out printB");
        }catch (Exception e ){

        }
    }



    public void run() {
        if(Thread.currentThread().getName().equals("Thread-0")) {
            printA();
            printB();
        }else{
            printB();
            printA();
        }

    }


    public static void main(String[] args) {

        Thread t1 = new Thread(new SynTestThread());
        Thread t2 = new Thread(new SynTestThread());
        t1.start();
        t2.start();

    }
}

````

Thread-0 in  printA
Thread-1 in  printB
Thread-1 out printB
Thread-0 out printA
Thread-0 in    printB
Thread-0 out printB
Thread-1 in    printA
Thread-1 out printA