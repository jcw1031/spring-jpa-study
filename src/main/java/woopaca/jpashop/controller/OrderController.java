package woopaca.jpashop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import woopaca.jpashop.domain.Member;
import woopaca.jpashop.domain.Order;
import woopaca.jpashop.domain.item.Item;
import woopaca.jpashop.repository.OrderSearch;
import woopaca.jpashop.service.ItemService;
import woopaca.jpashop.service.MemberService;
import woopaca.jpashop.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute OrderForm orderForm) {
        orderService.order(orderForm.getMemberId(), orderForm.getItemId(), orderForm.getCount());
        return "redirect:/orders";
    }

    @GetMapping("")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        log.info("orderList -> (orderSearch) = {}", orderSearch);
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/{orderId}/cancel")
    public String orderCancel(@PathVariable("orderId") Long orderId) {
        return null;
    }
}
