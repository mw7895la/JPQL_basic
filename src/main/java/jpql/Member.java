package jpql;

import javax.persistence.*;

@Entity(name = "Member")        //기본적으로 엔티티 이름은 클래스이름이랑 똑같다.
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //양방향 주의점,  toString() 할 때 연관관계 주인인 team 제외, 대상테이블에서도 연관관계 필드 마찬가지로 toString()시 제외할 것.
    //서로 무한 호출해서 stackoverflow 에러남.
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
