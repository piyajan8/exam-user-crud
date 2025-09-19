package com.macode101.exam.repository;

import com.macode101.exam.model.User;
import java.util.HashMap;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class InMemoryUserRepository implements UserRepository {
    
    private final HashMap<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void initializeData() {
        User user1 = new User();
        user1.setId(idGenerator.getAndIncrement());
        user1.setName("Leanne Graham");
        user1.setUsername("Bret");
        user1.setEmail("Sincere@april.biz");
        user1.setPhone("1-770-736-8031 x56442");
        user1.setWebsite("hildegard.org");
        users.put(user1.getId(), user1);
        
        User user2 = new User();
        user2.setId(idGenerator.getAndIncrement());
        user2.setName("Ervin Howell");
        user2.setUsername("Antonette");
        user2.setEmail("Shanna@melissa.tv");
        user2.setPhone("010-692-6593 x09125");
        user2.setWebsite("anastasia.net");
        users.put(user2.getId(), user2);
        
        User user3 = new User();
        user3.setId(idGenerator.getAndIncrement());
        user3.setName("Clementine Bauch");
        user3.setUsername("Samantha");
        user3.setEmail("Nathan@yesenia.net");
        user3.setPhone("1-463-123-4447");
        user3.setWebsite("ramiro.info");
        users.put(user3.getId(), user3);
        
        User user4 = new User();
        user4.setId(idGenerator.getAndIncrement());
        user4.setName("Patricia Lebsack");
        user4.setUsername("Karianne");
        user4.setEmail("Julianne.OConner@kory.org");
        user4.setPhone("493-170-9623 x156");
        user4.setWebsite("kale.biz");
        users.put(user4.getId(), user4);
        
        User user5 = new User();
        user5.setId(idGenerator.getAndIncrement());
        user5.setName("Chelsey Dietrich");
        user5.setUsername("Kamren");
        user5.setEmail("Lucio_Hettinger@annie.ca");
        user5.setPhone("(254)954-1289");
        user5.setWebsite("demarco.info");
        users.put(user5.getId(), user5);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }
    
    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
