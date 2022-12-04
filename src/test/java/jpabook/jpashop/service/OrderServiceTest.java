package jpabook.jpashop.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    //@PersistenceContext
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void orderOrderOrder() throws Exception{
        Member member = createMember();

        Book book = createBook("hi", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.ORDER.name(), getOrder.getStatus().name());
        Assertions.assertEquals(1, getOrder.getOrderItems().size());
        Assertions.assertEquals(10000*orderCount, getOrder.getTotalPrice());
        Assertions.assertEquals(8, book.getStockQuantity());

    }



    @Test
    public void overCount() throws Exception{
        Member member = createMember();
        Item item = createBook("hi", 10000, 10);

        int orderCount = 10;

        orderService.order(member.getId(), item.getId(), orderCount);
        fail("재고 수량이 부족해야 한다");

    }

    @Test
    public void cancelOrder() throws Exception{
        Member member = createMember();
        Book item = createBook("jpa", 10000, 10);
        int orderCnt = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCnt);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("CANCEL", getOrder.getStatus().name());
        assertEquals(10, item.getStockQuantity());
    }

    private Book createBook(String name, int orderPrice, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(orderPrice);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123"));
        em.persist(member);
        return member;
    }
}