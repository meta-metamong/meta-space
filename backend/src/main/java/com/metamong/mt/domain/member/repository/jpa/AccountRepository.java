package com.metamong.mt.domain.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.member.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
