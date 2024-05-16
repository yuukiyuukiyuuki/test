package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;



@Service
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private UserRepository userRepository;
	private HouseRepository houseRepository;

	public  FavoriteService(FavoriteRepository favoriteRepository,UserRepository userRepository , HouseRepository houseRepository) {
	this.favoriteRepository = favoriteRepository;
	this.userRepository = userRepository;
	this.houseRepository = houseRepository;
}

	@Transactional
	public void create(FavoriteRegisterForm favoriteRegisterForm, House house, User user) {
		Favorite favorite = new Favorite();
		
		favoriteRegisterForm.setHouseId(house.getId());
		favoriteRegisterForm.setUserId(user.getId());
		
		favorite.setHouse(houseRepository.getReferenceById(favoriteRegisterForm.getHouseId()));
		favorite.setUser(userRepository.getReferenceById(favoriteRegisterForm.getUserId()));
		
		favoriteRepository.save(favorite);
	}
	
	@Transactional
	public void delete(Integer houseId, Integer userId) {
	    favoriteRepository.deleteByHouseIdAndUserId(houseId, userId);
	}
}
