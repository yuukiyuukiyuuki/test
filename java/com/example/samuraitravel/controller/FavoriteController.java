package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteRepository favoriteRepository, HouseRepository houseRepository, UserRepository userRepository, FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
		this.favoriteService = favoriteService;
	}
	//お気に入り一覧
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)Pageable pageable, Model model, HttpSession httpSession) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritesPage = favoriteRepository.findByUser(user, pageable);
		httpSession.setAttribute("favoriteHouse", houseRepository.findAll());
		
		model.addAttribute("user", user);
		model.addAttribute("favoritesPage", favoritesPage);
		
		return "favorites/index";
	}
	//お気に入り登録
	 @PostMapping("/houses/{id}/create")
	    public String createFavorite(@PathVariable(name = "id") Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
	        Integer userId = userDetailsImpl.getUser().getId();
	        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        House house = houseRepository.findById(houseId).orElseThrow(() -> new RuntimeException("House not found"));
	        FavoriteRegisterForm form = new FavoriteRegisterForm();
	        form.setHouseId(house.getId());
	        form.setUserId(user.getId());
	        
	        favoriteService.create(form, house, user);
	        redirectAttributes.addFlashAttribute("message", "お気に入りに追加しました");
	        return "redirect:/houses/{id}";
	    }

    

    //お気に入り解除
	@PostMapping("/houses/{id}/delete")
	public String deleteFavorite(@PathVariable(name = "id") Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
	    Integer userId = userDetailsImpl.getUser().getId();
	    favoriteService.delete(houseId, userId);
	    redirectAttributes.addFlashAttribute("message", "お気に入りを解除しました");
	    return "redirect:/houses/{id}";
    }

}
