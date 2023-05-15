package com.example.gatewayService.service;

import com.example.gatewayService.dto.UserDTOResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserStorageService {
    private UserDTOResponse userDTOResponse;
    private boolean notUsed;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public UserDTOResponse getUserDTOResponse() {
        setFlag(false);
        return userDTOResponse;
    }

    public void setUserDTOResponse(UserDTOResponse userDTOResponse) {
        setFlag(true);
        this.userDTOResponse = userDTOResponse;
    }

    public boolean isNotUsed() {
        return notUsed;
    }

    public void waitForFlagChange() {
        lock.lock();
        try {
            while (!notUsed) {
                condition.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private void setFlag(boolean value) {
        lock.lock();
        try {
            notUsed = value;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
