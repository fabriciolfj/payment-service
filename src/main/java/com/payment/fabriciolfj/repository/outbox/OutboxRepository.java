package com.payment.fabriciolfj.repository.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEntity, Long> {

    @Modifying
    @Query(value = """
            update outbox
            set status = 'PROCESSING', locked_at = now()
            	    where id in (
            	      select id from outbox
             		    where status = 'PENDING'
             			  or (status = 'PROCESSING' and locked_at < now() - interval '2 minutes')
             		    order by created_at asc
                        	limit :batchSize
						for update skip locked)    	  
                    returning *
            """, nativeQuery = true)
    List<OutboxEntity> claimBatch(@Param("batchSize") int batchSize);
}
