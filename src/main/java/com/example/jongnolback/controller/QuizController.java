package com.example.jongnolback.controller;

import com.example.jongnolback.dto.QuestionDTO;
import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.dto.ResponseDTO;
import com.example.jongnolback.dto.UserDTO;
import com.example.jongnolback.entity.CustomUserDetails;
import com.example.jongnolback.entity.Question;
import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;
import com.example.jongnolback.service.QuizService;
import com.example.jongnolback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {
    private final UserService userService;
    private final QuizService quizService;

    @PostMapping("/newquiz")
    public ResponseEntity<?> newquiz(@RequestBody QuizDTO quizDTO ,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<QuizDTO> responseDTO = new ResponseDTO<>();
        try {
                User user = customUserDetails.getUser();
                QuizDTO newQuizDTO = quizService.createQuiz(quizDTO, user);
                responseDTO.setData(quizDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(100);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/solvequiz/{id}")
    public  ResponseEntity<?> solvequiz(@PathVariable Long id) {

        try {
            System.out.println("@@@@@@@@@@@ id : " + id);
            Quiz quiz = quizService.findById(id);
            System.out.println("@@@@@@@@@@@ get id : " + quiz.getId());
            System.out.println(quiz.getQuestions());
            return ResponseEntity.ok(quiz);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getquiz")
    public ResponseEntity<?> getQuizList(
            @RequestParam("searchCondition") String searchCondition,
            @RequestParam("searchKeyword") String searchKeyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        ResponseDTO<List<QuizDTO>> responseDTO = new ResponseDTO<>();
        try {
            List<QuizDTO> quizzes = quizService.getQuizzes(searchCondition, searchKeyword, offset, limit);

            boolean hasMoreData = quizzes.size() == limit;

            System.out.println(hasMoreData);
            responseDTO.setHasMore(hasMoreData);
            responseDTO.setItem(quizzes);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setErrorCode(401);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/getcountqp")
    public ResponseEntity<?> getQuizList() {
        Map<String, Object> responseData = new HashMap<>();
        try {
            long quizzesCount = quizService.getQuizCount();
            long usersCount = userService.getUserCount();

            responseData.put("usersCount", usersCount);
            responseData.put("quizzesCount", quizzesCount);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", 103);
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
