package com.example.demo.eneity.vo;

import com.example.demo.eneity.Contribution;
import com.example.demo.service.UserService;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@ToString
public class ContributionVo {
    @Autowired
    UserService userService;
    private int id;
    private String picture1;
    private String picture2;
    private String picture3;
    private String submitTime;
    private String username;
    private String content;

    public ContributionVo() {
    }

    public ContributionVo(Contribution contribution) {
        this.id = contribution.getId();
        this.picture1 = contribution.getPicture1();
        this.picture2 = contribution.getPicture2();
        this.picture3 = contribution.getPicture3();
        this.submitTime = contribution.getSubmitTime();
        this.content = contribution.getContent();

    }
}
