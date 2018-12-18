import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MyApplication
 * Class: AspectDebugLog
 * Created by PCD-B-1807-006 on 2018/10/23.
 * <p>
 * Description:
 */
@Aspect
public final class AspectDebugLog {
    public static final String GADGET_ASPECT_2 = "GadgetAspect2";
    private static HashMap<Integer, List<String>> depthTexts = new HashMap<>();
    private static HashMap<Integer, Integer> depthCounts = new HashMap<>();
    private static String prespace = "│";
    private String lastClassMethod = "Start";

    private static final String POINTCUT_BEFORE_METHOD =
            "execution(* com.example.pcd_b_1807_006.myapplication..*.*(..)) && " +
                    "!(" +
                    "execution(* com.example.pcd_b_1807_006.myapplication.MainEnumString..*(..)) || " +
                    "execution(* com.example.pcd_b_1807_006.myapplication.sub..*.*(..)) " +
                    ")";


    private static final String POINTCUT_AROUND_METHOD = POINTCUT_BEFORE_METHOD;
    private int logType1 = 1;
    private String argAll;

    @Pointcut(POINTCUT_BEFORE_METHOD)
    public void pointcutDebugTraceBefore() {}

    @Pointcut(POINTCUT_AROUND_METHOD)
    public void pointcutDebugTraceAround() {}

    @Before("pointcutDebugTraceBefore()")
    public void weaveDebugTraceBefore(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String currentClassName = utils.getOnlyClassName(methodSignature.getDeclaringType().getSimpleName());
        String currentMethodName = utils.getOnlyClassName(methodSignature.getName());
        //not use
        argAll = utils.getArgs(joinPoint, methodSignature);

        int tid = android.os.Process.myTid();

        List<String> dummyDepthText = utils.getdepthTexts(tid);

        lastClassMethod = utils.getLastClassMethod(lastClassMethod, dummyDepthText, logType1);

        if (utils.getdepthCounts(tid) < 1) {
            lastClassMethod = "Start";
        }

        LogPrinter.logBefore(currentClassName, currentMethodName, lastClassMethod);


        Log.d(GADGET_ASPECT_2,  utils.repeat(prespace, utils.getdepthCounts(tid) + 1) + "▼" + "(" + joinPoint.getSignature().getDeclaringType() + ")");
        Log.d(GADGET_ASPECT_2,  utils.repeat(prespace, utils.getdepthCounts(tid) + 1) + "┏ " + currentMethodName);


        dummyDepthText.add(currentClassName);
    }


    @Around("pointcutDebugTraceAround()")
    public Object weaveDebugTraceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String currentClassName = utils.getOnlyClassName(methodSignature.getDeclaringType().getSimpleName());
        String currentMethodName = utils.getOnlyClassName(methodSignature.getName());

        int tid = android.os.Process.myTid();
        List<String> dummyDepthText = utils.getdepthTexts(tid);

        depthCounts.put(tid, utils.getdepthCounts(tid) + 1);

        LogPrinter.logAroundBefore(currentClassName);

        if (argAll.length() > 0) {
            Log.d(GADGET_ASPECT_2, utils.repeat(prespace, utils.getdepthCounts(tid) + 1) + argAll);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        LogPrinter.logAroundAfter(currentClassName, currentMethodName, dummyDepthText, stopWatch.getTotalTimeMillis());
        Log.d(GADGET_ASPECT_2,  utils.repeat(prespace, utils.getdepthCounts(tid) + 1) + "result  " + result );
        Log.d(GADGET_ASPECT_2, utils.repeat(prespace, (utils.getdepthCounts(tid))) +
                "┗ " + currentMethodName +
                " : " + stopWatch.getTotalTimeMillis() + "ms");

        //after process
        depthCounts.put(tid, utils.getdepthCounts(tid) - 1);

        if(dummyDepthText.size() >0) {
            dummyDepthText.remove(dummyDepthText.size()-1);
        }
        depthTexts.put(tid, dummyDepthText);

        return result;
    }

    /**
     * Class representing a StopWatch for measuring time.
     */
    private class StopWatch {
        private long startTime;
        private long endTime;
        private long elapsedTime;

        public StopWatch() {
            //empty
        }

        private void reset() {
            startTime = 0;
            endTime = 0;
            elapsedTime = 0;
        }

        public void start() {
            reset();
            startTime = System.nanoTime();
        }

        public void stop() {
            if (startTime != 0) {
                endTime = System.nanoTime();
                elapsedTime = endTime - startTime;
            } else {
                reset();
            }
        }

        public long getTotalTimeMillis() {
            return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(endTime - startTime) : 0;
        }
    }

    private static class LogPrinter {

        private static final String GADGET_ASPECT_UML = "GadgetAspectUML";
        private static final String START = "Start";
        private static final String ACTIVATE = "activate ";
        private static final String DEACTIVATE = "deactivate ";

        private static void logBefore(String className, String methodName, String lastClassMethod ) {
            Log.d(GADGET_ASPECT_UML,   lastClassMethod + " -> " + className + " : " + methodName);


        }

        private static void logAroundBefore(String className) {
            Log.d(GADGET_ASPECT_UML, ACTIVATE + className);

        }

        private static void logAroundAfter(String className, String methodName, List<String> depthText, long estimate) {
            String returnClassName = START;
            if(depthText.size() > 1) {
                returnClassName = depthText.get(depthText.size()-2);
            }

            Log.d(GADGET_ASPECT_UML, className + " --> " + returnClassName + " : " + methodName + "  ( " + estimate + "ms )"); //### weaveDebugTraceBefore:
            Log.d(GADGET_ASPECT_UML, DEACTIVATE + className );

        }
    }

    private static class utils {


        public synchronized static String repeat(String string, int times) {
            StringBuilder out = new StringBuilder();

            while (times-- > 0) {
                out.append(string);
            }
            return out.toString();
        }


        private static String getOnlyClassName(String classname) {
            if (classname.indexOf("$") < 1) {
                return isEmptyName(classname);
            }
            String[] str = classname.split("\\\\$", 0);
            return isEmptyName(str[0]);
        }

        private static String getLastClassMethod(String lastClassMethod, List<String> dummyDepthText, int logType) {
            if(dummyDepthText.size() > 0) {
                return isEmptyName(dummyDepthText.get(dummyDepthText.size()-logType));
            }
            return lastClassMethod;
        }

        private static List<String> getdepthTexts(int tid) {
            if(!depthTexts.containsKey(tid)) {
                depthTexts.put(tid, new ArrayList<String>());
            }
            return depthTexts.get(tid);
        }

        private static int getdepthCounts(int tid) {
            if(!depthCounts.containsKey(tid)) {
                depthCounts.put(tid, 0);
            }
            return depthCounts.get(tid);
        }

        private static String isEmptyName(String methodName) {
            if (methodName.length() < 2) {
                return "NULL";
            }
            return methodName;
        }

        private static String getArgs(JoinPoint joinPoint, MethodSignature methodSignature) {
            Object[] objArray = joinPoint.getArgs();
            String[] sigParamNames = methodSignature.getParameterNames();
            int i = 0;
            String argName = "";
            StringBuilder argAll = new StringBuilder();
            for (Object obj : objArray) {
                if (obj == null) continue;
                argName = sigParamNames[i];
                i++;
                argAll.append(argName + ":[" + obj.toString() + "] , ");
            }

            if (argAll.length() > 1) {
                argAll.insert(0, "args  ");
            }
            return argAll.toString();
        }
    }
}