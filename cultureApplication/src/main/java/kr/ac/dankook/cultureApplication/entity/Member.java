package kr.ac.dankook.cultureApplication.entity;

import jakarta.persistence.*;
import kr.ac.dankook.cultureApplication.common.MemberRole;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member")
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Builder
    public Member(String name, String email, String password, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
