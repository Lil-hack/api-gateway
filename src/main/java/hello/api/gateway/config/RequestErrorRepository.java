package hello.api.gateway.config;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestErrorRepository extends CrudRepository<RequestError, String> {}