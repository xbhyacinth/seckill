package org.seckill.util;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Random;


public class Utils {
    public static final int CARTID_SHUFFLE = 200000;
    static ThreadLocal<Random> tr = new ThreadLocal<Random>();
    static{
        isDebug = isDebug();
    }
//    public static void close(AutoCloseable... list){
//
//        for(AutoCloseable closeable: list){
//            if(Constants.THREADLOCAL_MODEL && closeable instanceof Jedis){
//                return;
//            }
//            try {
//                if(closeable != null){
//                    closeable.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

    private static Boolean isDebug;

    public static boolean isDebug(){
        if(isDebug != null){
            return isDebug;
        }
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        boolean isDebug = false;
        for (String arg : args) {
            if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
                isDebug = true;
                break;
            }
        }
        return isDebug;
    }


    public static String uiToOuterToken(int uid){
        return new String(Integer.toString(500 -uid));
    }

    public static int outerTokenToUid(String token){
        return 500 - Integer.parseInt(token);
    }

    public static int shuffleCartId(int cartId){
        Random random = tr.get();
        if(random ==null){
            random = new Random();
            tr.set(random);
        }
        int seed = random.nextInt() % 100;
        seed = seed < 0?-seed:seed;
        return seed * CARTID_SHUFFLE + cartId;
    }

    public static int getRealCartId(int cartId){
        return cartId % CARTID_SHUFFLE;
    }
}
