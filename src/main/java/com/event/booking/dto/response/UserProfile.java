package com.event.booking.dto.response;


import com.event.booking.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String name;
    private String email;
    private String token;


    public UserProfile(User user) {
        this.name = user.getName();
        this.email = user.getEmail();

    }

}
