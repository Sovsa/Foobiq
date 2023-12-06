package com.anilsson.foobiq;

import com.anilsson.foobiq.service.Line;
import com.anilsson.foobiq.service.TrafikLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private TrafikLabService trafikLabService;

    @GetMapping("/list")
    public String getList(Model model) {
        List<Line> busLinesWithMostStops = trafikLabService.getBusLinesWithMostStops(10, 1);
        model.addAttribute("busLines", busLinesWithMostStops);
        return "buslines";
    }
}
