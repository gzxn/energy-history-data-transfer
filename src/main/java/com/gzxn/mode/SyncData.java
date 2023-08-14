package com.gzxn.mode;

public abstract class SyncData {
    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 检查配置及连接
     */
    protected abstract void check();

    /**
     * 开始同步
     */
    protected abstract void startSync();

    public final void sync() {
        // 正在同步中
        if (!this.isComplete()) {
            return;
        }
        // 开始同步
        this.check();
        if (this.isCheckOk()) {
            this.init();
            this.startSync();
        }
    }

    /**
     * 检测通过
     *
     * @return
     */
    protected boolean isCheckOk() {
        return false;
    }

    /**
     * 同步完成
     *
     * @return
     */
    protected boolean isComplete() {
        return false;
    }

}
