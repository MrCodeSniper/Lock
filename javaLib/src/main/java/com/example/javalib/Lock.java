package com.example.javalib;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {


    String url="https://blog.csdn.net/qq_39521554/article/details/81130442";

    /**
     * 这里默认将APP当做单进程进行分析
     * 同步建立在多线程基础上
     * 线程的一些重要函数
     * 1.start 开始执行该线程
     * 2.stop 强制结束该线程执行
     * 3.join方法，等待该线程结束
     * 4.sleep该线程进入等待
     * 5.run 直接执行线程的run()方法
     * 6.start 线程调度的执行线程的run()方法
     *
     * -----------------------------------
     *
     * wait和notify是Object方法
     * wait和sleep的区别是 wait会释放对象锁 而sleep不会
     *
     *-----------------------------------
     *
     * 线程状态
     *
     * 1.新建  ----新建线程对象，并没有调用start()方法之前
     * 2.就绪  ----调用start()方法之后线程就进入就绪状态，但是并不是说只要调用start()方法线程就马上变为当前线程，在变为当前线程之前都是为就绪状态。值得一提的是，线程在睡眠和挂起中恢复的时候也会进入就绪状态哦。
     * 3.运行  ----线程被设置为当前线程，开始执行run()方法。就是线程进入运行状态
     * 4.阻塞 死亡  ----线程被暂停，比如说调用sleep()方法后线程就进入阻塞状态  ----线程执行结束
     * 5.死亡回到新建 阻塞回到就绪
     *
     *----------------------------------
     *
     * 锁类型
     *
     * 可重入锁：在执行对象中所有同步方法不用再次获得锁  ,线程可以进入任何一个它已经拥有的锁所同步着的代码块
     *
     * //通俗解释 打水 同个家族可以直接排在最前面
     *
     * 可中断锁：在等待获取锁过程中可中断
     *
     * 公平锁： 按等待获取锁的线程的等待时间进行获取，等待时间长的具有优先获取锁权利
     *
     * //先到的先打水 公平  非公平 晚到的可以在刚好打完水时尝试获取 如果成功就排到就排到前面 失败就去排队
     *
     * 读写锁：对资源读取和写入的时候拆分为2部分处理，读的时候可以多线程一起读，写的时候必须同步地写 //特殊的锁
     *
     * 乐观锁： 假设不会发生并发冲突，直接不加锁去完成某项更新，如果冲突就返回失败。
     *
     * //CAS机制，简单来说会有三个操作数，当前内存变量值V，变量预期值A，即将更新值B，当需要更新变量的时候，会直接将变量值V和预期值A进行比较，如果相同，则直接更新为B；如果不相同，则当前变量值V刷新到预期值中，然后重新尝试比较更新。
     * //CAS 挺重要的概念  Compare And Swap 比较和交换
     *
     * 悲观锁：假设一定会发生并发冲突，通过阻塞其他所有线程来保证数据的完整性。   //Synchronized多线程同步，具有排他性，也会容易产生死锁。
     * ----------------------------------
     *
     *
     * synchronized与lock的区别
     *
     * 从存在上来说 synchronized 是jvm层面的 是java中的一个关键字  lock是一个接口规定了一个锁所需具备的功能
     *
     * 从使用上来说 synchronized 可以修饰变量 代码块 函数 类等 使用它意味着将加锁和解锁都交给jvm 无法感知锁状态
     *
     * 默认是可重入不可中断 非公平锁 获取锁时如果被占用会一直等待
     *
     * 交给JVM的另外一个好处是 JVM帮我们自动释放锁
     *
     * 底层：用字节码指令来控制程序 映射为指令 monitorenter和monitorexit
     *
     *
     * 而Lock灵活性大的多 首先它是个接口 使用时根据业务需要 选择适合的实现锁
     * 这样就导致其 可重入 可判断 可公平  而且可以调用方法知道锁的状态
     *  可以更灵活的选择是否堵塞式等待锁
     *
     * 需要手动释放锁而且需要考虑到所有的情况 不然容器造成死锁
     *
     *
     *
     *
     *
     *
     */


    //可重入锁
    ReentrantLock reentrantLock=new ReentrantLock();



    private void method(Thread thread){
        reentrantLock.lock();
        System.out.println("线程名"+thread.getName() + "获得了锁");
        reentrantLock.unlock();
        System.out.println("线程名"+thread.getName() + "释放了锁");
    }

    private void method2(Thread thread){
        if(reentrantLock.tryLock()){
            try {
                System.out.println("线程名"+thread.getName() + "获得了锁");
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                System.out.println("线程名"+thread.getName() + "释放了锁");
                reentrantLock.unlock();
            }
        }else { //如果被占用
            System.out.println("我是"+Thread.currentThread().getName()+"有人占着锁，我就不要啦");
        }
    }


    /**
     * Lock可以控制自身是否公平
     *
     *  public ReentrantLock() {
     *         this.sync = new ReentrantLock.NonfairSync(); //默认非公平锁
     *     }
     *
     */
    private void failOrNot(){

    }


    ReadWriteLock readWriteLock;

    Sync sync; //同步接口 包含操作1.acquire 获得锁 2.attempt尝试获取锁 3.release释放锁

    java.util.concurrent.locks.Lock lock;
    //锁接口 规定了一个锁必须包含的功能 包含操作1.获取锁2.释放锁 3.锁中断去做其他事情 4.trylock 如果获取锁的时候锁被占用就返回false




    public static void main(String[] args){
        final Lock lockTest = new Lock();

        //线程1
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lockTest.method2(Thread.currentThread());
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                lockTest.method2(Thread.currentThread());
            }
        }, "t2");

        t1.start();
        t2.start();

        /**
         *
         * 线程名t2获得了锁
         * 线程名t2释放了锁
         * 线程名t1获得了锁
         * 线程名t1释放了锁
         *
         * 可重入锁 即使一个代码块已经被一个线程锁住 另外一个线程也可以执行这个代码块
         *
         */





    }




}
