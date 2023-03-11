package com.example.demo.Util;



import com.example.demo.eneity.Goods;
import com.example.demo.eneity.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RecommendUtil {

    private Map<Double, Integer> computeNearestNeighbor(int userId, List<User> users) {
        Map<Double, Integer> distances = new TreeMap<>();
        User u1 = new User();
        u1.setId(userId);
        for (User user:users) {
            if (userId==user.getId()) {
                u1 = user;
            }
        }
        for (int i = 0; i < users.size(); i++) {
            User u2 = users.get(i);

            if (u2.getId()!=userId) {
                double distance = pearson_dis(u2.goodList, u1.goodList);
                distances.put(distance, u2.getId());
            }
        }
        System.out.println("该用户与其他用户的皮尔森相关系数 -> " + distances);
        return distances;
    }



    private double pearson_dis(List<Goods> rating1, List<Goods> rating2) {
        int n=rating1.size();
        List<Integer> rating1ScoreCollect = rating1.stream().map(A -> A.getScore()).collect(Collectors.toList());
        List<Integer> rating2ScoreCollect = rating2.stream().map(A -> A.getScore()).collect(Collectors.toList());

        double Ex= rating1ScoreCollect.stream().mapToDouble(x->x).sum();
        double Ey= rating2ScoreCollect.stream().mapToDouble(y->y).sum();
        double Ex2=rating1ScoreCollect.stream().mapToDouble(x->Math.pow(x,2)).sum();
        double Ey2=rating2ScoreCollect.stream().mapToDouble(y->Math.pow(y,2)).sum();
        double Exy=0;
        try{
             Exy= IntStream.range(0,n-1).mapToDouble(i->rating1ScoreCollect.get(i)*rating2ScoreCollect.get(i)).sum();
        }catch (Exception e){
            return 0.0;
        }
        double numerator=Exy-Ex*Ey/n;
        double denominator=Math.sqrt((Ex2-Math.pow(Ex,2)/n)*(Ey2-Math.pow(Ey,2)/n));
        if (denominator==0) return 0.0;
        return numerator/denominator;
    }


    public List<Goods> recommend(int userId, List<User> users) {

        Map<Double, Integer> distances = computeNearestNeighbor(userId, users);
        int nearest = distances.values().iterator().next();


        User neighborRatings = new User();
        for (User user:users) {
            if (nearest==user.getId()) {
                neighborRatings = user;
            }
        }


        User userRatings = new User();
        for (User user:users) {
            if (userId==user.getId()) {
                userRatings = user;
            }
        }

        List<Goods> recommendationMovies = new ArrayList<>();
        for (Goods movie : neighborRatings.goodList) {
            if (userRatings.find() == null) {
                recommendationMovies.add(movie);
            }
        }
        Collections.sort(recommendationMovies);
        return recommendationMovies;
    }
}