//package com.cocoh.movie.service;
//
//import com.cocoh.movie.Entity.Movie;
//import com.cocoh.movie.Entity.User;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.Root;
//import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//
//import java.util.function.Predicate;
//
//@Service
//public class QuestionService {
//
//    private Specification<Question> search(String keyword){
//        return new Specification<Question>() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);
//                Join<Question, Movie> u1 = q.join("movie");
//
//                return cb.or(cb.like(u1.get("movie_name"), "%"+keyword+"%"));
//            }
//        }
//    }
//
//}
