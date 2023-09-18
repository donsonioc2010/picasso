package picasso.server.api.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import picasso.server.api.admin.dto.response.BoardResponseDTO;
import picasso.server.api.admin.service.DefaultAdminService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/list")
    public String adminBoardlist() {
        List<BoardResponseDTO> list = new ArrayList<>();
        list.add(BoardResponseDTO.builder()
                .id(1L)
                .startAmount(10000L)
                .title("제목1")
                        .painter("고흐")
                        .created_at(Timestamp.valueOf("2023-09-15"))


                .build());


        return "admin/list";
    }

    @GetMapping("/detail")
    public String adminBoarddetail() {
        List<BoardResponseDTO> detail = new ArrayList<>();
        detail.add(BoardResponseDTO.builder()
                .photo_link("www.url")
                .id(1L)
                .startAmount(10000L)
                .title("제목1")
                .painter("고흐")
                .created_at(Timestamp.valueOf("2023-09-15"))
                .content("이 그림은 1928년 영국에서")
                .status("대기중")
                .build());
    return "admin/detail";
    }


    }
}
