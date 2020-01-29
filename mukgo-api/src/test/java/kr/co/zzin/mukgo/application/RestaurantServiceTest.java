package kr.co.zzin.mukgo.application;

import kr.co.zzin.mukgo.domain.*;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;

public class RestaurantServiceTest {

    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        mockRestaurantRepository();
        mockMenuItemRepository();

        restaurantService = new RestaurantService(
                restaurantRepository, menuItemRepository);
    }

    private void mockMenuItemRepository() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Kimchi"));
        given(menuItemRepository.findAllByRestaurantId(1004L)).willReturn(menuItems);
    }

    private void mockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant(1004L, "Bob zip", "Seoul");
        restaurants.add(restaurant);
        given(restaurantRepository.findAll()).willReturn(restaurants);
        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));

    }

    @Test
    public void getRestaurants(){
        List<Restaurant> restaurants = restaurantService.getRestaurants();
        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId(), is(1004L));
    }

    private Restaurant getRestaurant(List<Restaurant> restaurants) {
        return restaurants.get(0);
    }

    @Test
    public void getRestaurant(){
       Restaurant restaurant = restaurantService.getRestaurant(1004L);

       assertThat(restaurant.getId(), is(1004L));

       MenuItem menuItem = restaurant.getMenuItems().get(0);
       assertThat(menuItem.getName(), is("Kimchi"));
    }

    @Test
    public void addRestaurant(){
        Restaurant restaurant = new Restaurant("Beryong", "Busan");
        Restaurant saved= new Restaurant(1234L, "Beryong", "Busan");

        given(restaurantRepository.save(any())).willReturn(saved);

        Restaurant created =  restaurantService.addRestaurant(restaurant);

        assertThat(created.getId(), is(1234L));
    }

    @Test
    public void updateRestaurant(){
        Restaurant restaurant = new Restaurant(1004L, "Bobzip", "Seoul");

        given(restaurantRepository.findById(1004L))
                .willReturn(Optional.of(restaurant));

        Restaurant updated = restaurantService.updateRestaurant(1004L, "DDukzip", "Busan");

        assertThat(restaurant.getName(), is("DDukzip"));
        assertThat(restaurant.getaddress(), is("Busan"));
    }



}