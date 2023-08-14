package com.gzxn.utils;

/**
 * 异常信息工具类
 */
public class ExceptionUtil {

    /**
     * 获取完整的异常信息，递归调用
     *
     * @param throwable
     * @return
     */
    public static String getExceptionInfo(Throwable throwable) {
        StringBuilder info = new StringBuilder();
        // 获取异常的类名和详细信息
        String exceptionName = throwable.getClass().getName();
        String message = throwable.getMessage();
        info.append("Exception: ").append(exceptionName);
        if (message != null) {
            info.append("\nMessage: ").append(message);
        }
        // 获取异常堆栈轨迹
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            info.append("\nStack Trace:\n");
            for (StackTraceElement element : stackTrace) {
                info.append("\t").append(element.toString()).append("\n");
            }
        }
        // 获取异常的原因
        Throwable cause = throwable.getCause();
        if (cause != null) {
            info.append("\nCause:\n");
            info.append(getExceptionInfo(cause));
        }
        return info.toString();
    }

}
