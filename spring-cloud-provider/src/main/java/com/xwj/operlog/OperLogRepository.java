package com.xwj.operlog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.OperLog;

public interface OperLogRepository extends JpaRepository<OperLog, Long>{

}
