package com.leggett.glorious.photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, Long>{
    
    Photo findById(long id);

    Page<Photo> findAll(Pageable imagePage);
}

