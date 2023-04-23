package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            /** 1:N 페이징은 위험하다  N:1로 해서 페이징 해라.*/
            /*Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();
            //String query = "select t from Team t join t.members";       // team을 기준으로 조인하여 select t 가 아닌.
            //String query = "select m from Member as m join fetch m.team as t";    //이렇게 뒤짚으면 회원에서 팀으로 가는건 N :1이니 페이징 안전.
            //정 페이징 하고 싶으면 (1:N) join fetch t.members m을 과감하게 뺀다.
            String query = "select t from Team t";
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            //join fetch t.members m 과감히 뺀 상태.
            //결과를 보면, 처음에 Team에 관한 select 쿼리가 나갔다. ( limit 잘 나왔고)
            //결과 size 2개 나왔다.
            //Team에서 Member가 Lazy로 걸리니까, for 루프 돌 떄 TeamA가 처음에 돌면 TeamA와 연관된 멤버 2개를 Lazy 로딩 했다. 그래서 select 로 불러 온거고,
            //Team에 대해서 2번째 루프를 돌 때 , TeamB는 연관된 멤버 1개를 Lazy 로딩했다 그래서 select 호출됨.  이런식으로 하면 성능문제가 있다.

            //이번엔 Team 에 @BatchSize(size=100) 를 추가해보자.
            //select 에서 member를 가져오는데 ?,? 2개가 있따 teamA와 teamB연관된 멤버를 다 가져온 것이다.
            //Team을 가져올때 member는 현재 Lazy 로딩 상태다. Lazy로딩인 member를 끌고 올 때 result에 담긴 팀을 한번에 인쿼리로 100개씩 날린다. 지금은 2개라 다 나온것이다. 위에 문제가 해결된다.
            //batch fecth size를 쓰면, 쿼리를 N+1이 아니라 테이블 수만큼 맞출수가 있다.


            System.out.println("result.size() = " + result.size());
            for (Team team : result) {
                System.out.println("team.getName() = " + team.getName() + " , members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }*/


            /** 일반 조인*/
            /*Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select t from Team t join t.members";       //일반 조인은 연관된 엔티티를 함께 조회하지 않는다. 근데 어쨋든 로우 수가 3개로 뻥튀기 된다.
            //그리고 member에 관한 데이터가 로딩시점에 로딩이 안돼서 밑에 for문에서 select 문이 나가게 된다.
            List<Team> result = em.createQuery(query, Team.class).getResultList();


            System.out.println("result.size() = " + result.size());

            //팀 조회 쿼리 한번, 조회된 팀이 자신에게 속한 멤버를 불러오는데 N번의 조회 쿼리가 발생. 또한 회원 수 대로 팀의 데이터가 중복으로 들어가므로 데이터 뻥튀기 발생.
            for (Team team : result) {
                System.out.println("team.getName() = " + team.getName() + " , members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }*/

            
            /** Fetch Join과 Distinct*/
            /*Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //패치 조인과 distinct
            String query = "select distinct t from Team t join fetch t.members";     // select t from Team t 는 size는 2다. 근데 패치 조인하면서 데이터 뻥튀기돼서 이땐 size 3이다.
            //지금 강의자료70장의 테이블 위아래의 데이터가 모두 같아야 distinct가 먹힌다. sql 쿼리만으로는 우리가 원하는 중복을 못없앤다.
            //아까 말한 2번째 기능, 애플리케이션에서 중복 제거시도 -> 같은 식별자 가진 Team 엔티티 제거.
            List<Team> result = em.createQuery(query, Team.class).getResultList();

            System.out.println("result.size() = " + result.size());*/


            /** Fetch Join */
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //컬렉션 패치 조인     여긴 1:N
            /*String query = "select t from Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class).getResultList();

            for (Team team : result) {
                System.out.println("team.getName() = " + team.getName() + " , members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
                //team.getName() = 팀A , members = 2
                //team.getName() = 팀A , members = 2 //왜 중복으로 출력되냐?  컬렉션일때 이걸 조심. DB입장에서 1:N 는 N만큼 데이터 뻥튀기 된다.(팀A로된 멤버2명 그래서 2줄) .DB에 쿼리 날렸더니 2줄나온것. JPA는 DB에서 나온 결과 수 만큼 돌려줘야 한다.
                //팀 A 입장에선 Member와 조인하게 되면 강의자료 67장 가운데 그림처럼 만든다. 줄 수가 2줄이 된다. on team0_.TEAM_ID=members1_.TEAM_ID 회원1,2에 대해 팀A 2번 가져온것.
                // 영속성 컨텍스트에는 팀A가 하나지만,  조회한 컬렉션에는 같은 주소값을 가진 팀A가 2줄이 들어간다.  (** 다대일은 이런 일 안 생긴다)
                //team.getName() = 팀B , members = 1 //패치 조인을 했기 때문에 팀 A 입장에선 회원1,2 를 가지고 있다.
            }*/

            //패치 조인
            /*String query = "select m from Member m join fetch m.team";  //Member를 조회하긴 할 건데, fetch라는건 한번에 가져온다는 뜻. 즉, member와 team을 조인해서 한번에 가지고와.
            List<Member> result = em.createQuery(query, Member.class).getResultList();
            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + " , " + member.getTeam().getName());
                //이때 getTeam()은 프록시가 아니다  팀은 실제 엔티티가 담긴 것이다.(지연 로딩을 해도 패치 조인이 우선이다 그래서 team이 프록시가 아닌 실제 엔티티인 것) query의 결과 데이터가(강의자료 64장) 이미 영속성 컨텍스트에 채워져 있는 것이다.

                //아래 그냥 조회의 단점때문에 패치 조인을 사용한다.
                //select 쿼리는 1번 나감.
            }*/


            //그냥 조회
            /*String query = "select m from Member m";

            List<Member> result = em.createQuery(query, Member.class).getResultList();
            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + " , " + member.getTeam().getName());
                //회원1, 팀A를 (SQL)쿼리로 가져온다 팀A가 영속성 컨텍스트에 없기 때문,
                //회원2, 팀A가 이제 1차캐시에 있다 1차캐시에 가져옴.
                //회원3, 팀B를 (SQL) 영속성 컨텍스트에 보니까 없으니 쿼리를 보내서 결과를 반환해서 1차캐시에 보관.

                //회원 100명이면...-> 최악의 경우 쿼리가 100번 나간다.  N+1이다.  1은 첫번째 날린 쿼리, 1번째 날린 쿼리의 결과를 N번만큼 날리는 것.
            }*/

            /** 경로 표현식  절대 @묵시적 조인@ 쓰지 마라,   @명시적 조인@을 사용해라*/
            /*Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();*/
            //상태 필드.  경로의 끝이다. username에서 .점을 찍어서 또 어디 갈 수 있는게 아니니까. 더이상 탐색 불가.
            //String query = "select m.username from Member m";
            //단일 값 연관 경로 m.team으로 할 시 Member와 연관된 소속된 Team을 가져오겠다는 것. 이러면 조인이 일어난다. m.team -> m.team.name  name은 상태필드 이제 탐색 끝.
            //String query = "select m.team from Member m";
            /*List<Team> result = em.createQuery(query, Team.class).getResultList();
            for (Team team1 : result) {
                System.out.println("team1 = " + team1);
            }*/




            //컬렉션 값 연관 경로는 탐색을 못한다.  이건 일 대 다 관계인데. 컬렉션에서 한두개가 아니고 여러개가 들어있을텐데 그중에서 뭘 꺼내거 어떤 필드를 꺼낼지 난감해서 JPA에서 제약을 걸었다.
            /*String query = "select t.members From Team t";

            List result = em.createQuery(query, Collection.class).getResultList();

            for (Object o : result) {
                System.out.println("o = " + o);
            }*/
            //컬렉션 값 연관 경로는 탐색을 못하는데, 대신에 명시적인 조인을 하면 가능하다.  t.members를 명시적 조인하고 별칭을 얻으면 이걸로 username을 탐색할 수 있다.
            /*String query = "select m.username from Team as t join t.members as m";
            List<Member> result = em.createQuery(query, Member.class).getResultList();*/

            /** 기본 함수 */
            /*Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            //String query = "select concat('a','b') From Member m";
            //String query = "select substring(m.username,2,3) From Member m";
            //String query = "select locate('de','abcdefg') from Member m"; //숫자 4를 돌려준다.
            //String query = "select size(t.members) from Team t";        //컬렉션의 크기.

            //이건 안쓰는게 좋다.
            *//*@OrderColumn
            String query = "select index(t.members) from Team t";*//*

            //사용자 정의 함수.  group_concat는 h2가 기본으로 가지고 있는것.  이런식으로 등록하여 사용하면 된다.
            String query = "select function('group_concat',m.username) from Member m";

            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }*/


            /** CASE , COALESCE, NULLIF - 이 함수들은 JPA에서 제공하는 표준함수라 어느 DB 벤더에서든 가능하다.*/
            /*Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);
            em.persist(member);


            em.flush();
            em.clear();*/
            /*String query = "select " +
                    "case when m.age <=10 then '학생요금' " +
                    "when m.age >=60 then '경로요금' " +
                    "else '일반요금' end as fee " +
                    "from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }*/

            /*String query = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }*/

            /*String query= "select nullif(m.username, '관리자') as username from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s :result){
                System.out.println("s = " + s);
            }*/


            /** ENUM*/
            /*Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);
            em.persist(member);


            em.flush();
            em.clear();

            *//*String query="select m.username, 'HELLO', true from Member m where m.memberType = jpql.MemberType.ADMIN";
            List<Object[]> resultList = em.createQuery(query).getResultList();*//*
            String query = "select m.username, 'HELLO', true from Member m where m.memberType = :userType";
            List<Object[]> resultList = em.createQuery(query).setParameter("userType", MemberType.ADMIN)
                    .getResultList();


            for (Object[] objects : resultList) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
            }*/

            /** inner 조인 , left 조인  */

            /*Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);


            em.persist(member);


            em.flush();
            em.clear();


            *//*String sql = "select m from Member m left outer join m.team as t";
            List<Member> result = em.createQuery(sql, Member.class)
                    .getResultList();
            //from Member m join Team t 하게되면 오류가 날 것.  근데 보통 조인에 on 조건이 붙는데 여긴 안써줬다. -> 객체의 연관관계가 테이블에서는 fk pk를 조인하는 것이기 때문이다.
            //JPQL은 객체를 조인하는 것이고 이건 테이블입장에서는 fk와 pk를 조인하는 것이 된다.
            //Join 문  다음에 team에 대한 select 가 나간다( why? 즉시로딩으로 되어있기 때문)
            //inner 는 생략할 수 있다.
            //left outer join 에서 outer도 생략할 수 있다.


            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }*//*
            String sql = "select m from Member m left outer join m.team as t on t.name='teamA'";
            List<Member> result = em.createQuery(sql, Member.class)
                    .getResultList();       //실행 결과 쿼리를 보면
            //on member0_.TEAM_ID=team1_.TEAM_ID and (team1_.name='teamA')

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }*/


                /** 페이징 처리*/

            /*for(int i=0; i<100; i++) {
                Member member = new Member();
                member.setUsername("member"+i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();


            //sorting이 되면서 순서대로 가져와야 되니까..
            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();//내림차순하고 0번 위치부터 10개 가져온다.

            // setFirstResult(0) 일땐, order by member0_.age desc limit ?  찍힘.
            // setFirstResult(1) 일땐, order by member0_.age desc limit ? offset ? 찍힘.  둘다 H2디비의 방언임.

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }*/


                /** 프로젝션 */
            /*Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //쿼리가 이렇게 나가는데 m이 엔티티잖아. 반환도 엔티티다. 그럼 얘는 flush,clear후에 가져오면 영속성 컨텍스트에서 관리가 될까? 반환된 결과들이 다 영속성 컨텍스트에 관리된다.
            *//*List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20);*//*      //값이 바뀌면 영속성 컨텍스트에서 관리 된다는 것. -> 바뀐다.

                 *//*List<Team> result2 = em.createQuery("select t from Member m join m.team as t", Team.class)
                    .getResultList();       //멤버와 관련된 team 이거 실행하면 join 쿼리 나감.*//*

                 *//*em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();       //해당 쿼리의 결과로 o.address를 가져오기 때문에 이 데이터를 받기  위해 Address.class가 사용된다. Address는 엔티티에 소속된거라 from Address a 이런식으로 못한다.*//*

                 *//*em.createQuery("select m.username, m.age from Member m"*//**//*, Member.class*//**//*)
                    .getResultList();       //스칼라의 경우 조회되는 타입이 제각각이라 2번재 파라미터는 빼주자.*//*

            //바로 위에서 처럼 응답 타입이 2개일땐 어떻게 할까? // 반환 타입이 명확하지 않다.
            *//*List resultList = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            Object o = resultList.get(0);
            Object[] res = (Object[]) o;
            System.out.println("username = " + res[0]);
            System.out.println("age = " + res[1]);*//*

            //위에를 그나마 간편하게
            *//*List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            Object[] res = resultList.get(0);
            System.out.println("username = " + res[0]);
            System.out.println("age = " + res[1]);*//*

            //제일 깔끔한것.  new 명령어로 조회, MemberDTO를 엔티티와 같은 속성으로 만든다. 엔티티가 아닌 MemberDTO로 뽑고싶다. 그럼 타입을 주고, new 연산자를 써준다. + 패키지명
            //이 조회는 엔티티가 아닌 MemberDTO로 값을 조회하는 것이어서. 영속성 컨텍스트에서 관리가 안된다.
            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());*/

                /** 파라미터 바인딩  되도록이면, 이름 기준을 사용하자.*/
            /*Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //TypedQuery<Member> query = em.createQuery("select m from Member m where m.username= :username", Member.class); //이렇게 하거나 메서드 Chain을 이용하자.
            //query.setParameter("username", "member1");      //바인딩.

            Member result = em.createQuery("select m from Member m where m.username= :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();


            //System.out.println("singleResult = " + singleResult.getUsername());

            System.out.println("result = " + result);*/

                /** type query , query
                 *  getResultList()는 결과가 없으면 빈 리스트 반환(NPE 신경 안써도 된다.)
                 *  getSingleResult()는 결과없거나 둘 이상이면 예외 터짐.*/
            /*Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);//타입 정보는 기본적으로 엔티티를 줘야함.
            //TypedQuery<String> query2 = em.createQuery("select m.username, m.age from Member m", String.class);//username은 스트링이고 age는 int다  그래서 String으로 통일하던가 하면 typeQuery 로 사용 가능.
            //Query query3 = em.createQuery("select m.username, m.age from Member m");

            //쿼리 결과가 하나일떄
            Member result = query.getSingleResult();
            System.out.println("result = " + result);       //결과가 2개 이상일떄 Exception은 이해한다..! 그런데, 결과가 없는데 Exception이 터지면 좀 그렇다.. 또 막 try~catch 해야하는데.. 나중에  Spring Data Jpa를 쓰면 된다.

            //쿼리 결과가 여러개 일때.
            *//*List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }*/

                tx.commit();
            } catch(Exception e){
                tx.rollback();
                e.printStackTrace();
            } finally{
                em.close();     //데이터베이스 커넥션을 물고 동작하기 때문에 사용 다하면 닫아줘야한다.
            }

            emf.close();
        }
    }

