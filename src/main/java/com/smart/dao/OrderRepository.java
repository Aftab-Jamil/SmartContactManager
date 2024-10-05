package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Orders;
import java.util.List;


public interface OrderRepository extends JpaRepository<Orders,Integer>{
	@Query("from Orders where orderId=:id")
	public Orders getByorderid(@Param("id") String orderId);
	
	

}
