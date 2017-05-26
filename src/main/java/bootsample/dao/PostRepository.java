package bootsample.dao;

import org.springframework.data.repository.CrudRepository;

import bootsample.model.Post;;

public interface PostRepository extends CrudRepository<Post, Integer>{

}
