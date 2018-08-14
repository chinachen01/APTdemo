package com.slyser.api;

import android.content.Context;
import android.content.Intent;

import com.slyser.annotation.IRouterGroup;

import java.lang.reflect.InvocationTargetException;

/**
 * author: chenyong(<a href="chenyong@danlu.com">chenyong@danlu.com</a>)<br/>
 * version: 1.0.0<br/>
 * since: 2018/8/6 下午7:01<br/>
 *
 * <p>
 * 内容描述区域
 * </p>
 */
public final class SRouter {
    private static final String ROUTER_GROUP_PREFIX = "com.slyser.router";
    private static class Holder {
        private static SRouter INSTANTCE = new SRouter();
    }

    public static SRouter getInstance() {
        return Holder.INSTANTCE;
    }

    public void navigate(Context context, String routerName) {
        Intent intent = new Intent(context, getActivityClass(routerName));
        context.startActivity(intent);
    }

    public Class<?> getActivityClass(String routerName) {
        try {
            IRouterGroup routerGroup = (IRouterGroup) Class.forName(ROUTER_GROUP_PREFIX + ".Router$$GroupApp").getConstructor().newInstance();
            String activityName = routerGroup.getActivityName(routerName);
            return Class.forName("com.slyser.aptdemo.TwoActivity");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
