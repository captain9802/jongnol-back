package com.example.jongnolback.controller;

import com.example.jongnolback.common.FileUtils;
import com.example.jongnolback.dto.QuestionDTO;
import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.dto.ResponseDTO;
import com.example.jongnolback.dto.UserDTO;
import com.example.jongnolback.entity.CustomUserDetails;
import com.example.jongnolback.entity.Question;
import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;
import com.example.jongnolback.repository.QuestionRepository;
import com.example.jongnolback.service.QuestionService;
import com.example.jongnolback.service.QuizService;
import com.example.jongnolback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private final QuestionService questionService;
    private final FileUtils fileUtils;

    @PostMapping("/newquiz")
    public ResponseEntity<?> newquiz(@RequestBody QuizDTO quizDTO ,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<QuizDTO> responseDTO = new ResponseDTO<>();
        try {
            User user = customUserDetails.getUser();

            MultipartFile thumbnailFile = fileUtils.convertBase64ToMultipartFile(quizDTO.getThumbnail(), "thumbnail.png");

            String uploadedThumbnailPath = fileUtils.uploadFile(thumbnailFile, "quiz-thumbnails/");

            quizDTO.setThumbnail(uploadedThumbnailPath);

            QuizDTO newQuizDTO = quizService.createQuiz(quizDTO, user);

            responseDTO.setData(newQuizDTO);
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
            Quiz quiz = quizService.findById(id);
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

            for (QuizDTO quiz : quizzes) {
                String thumbnailUrl = fileUtils.getObjectUrl(quiz.getThumbnail());
                quiz.setThumbnail(thumbnailUrl);
            }

            boolean hasMoreData = quizzes.size() == limit;

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

    @GetMapping("/getmyquiz")
    public ResponseEntity<?> getMyQuizList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        ResponseDTO<List<QuizDTO>> responseDTO = new ResponseDTO<>();
        try {
            List<QuizDTO> quizzes = quizService.getMyQuizzes(customUserDetails, offset, limit);

            for (QuizDTO quiz : quizzes) {
                String thumbnailUrl = fileUtils.getObjectUrl(quiz.getThumbnail());
                quiz.setThumbnail(thumbnailUrl);
            }

            boolean hasMoreData = quizzes.size() == limit;

            responseDTO.setHasMore(hasMoreData);
            responseDTO.setItem(quizzes);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setErrorCode(402);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @DeleteMapping("/deletequiz/{id}")
    public ResponseEntity<?> deletequiz(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ResponseDTO<QuizDTO> responseDTO = new ResponseDTO<>();
        try {
            Quiz quiz = quizService.findById(id);

            if (quiz.getUser().getId() == customUserDetails.getUser().getId()) {
                if (quiz.getThumbnail() != null && !quiz.getThumbnail().isEmpty()) {
                    System.out.println("quiz.getThumbnail() :" +quiz.getThumbnail());
                    fileUtils.deleteFile(quiz.getThumbnail());
                }
                if (quiz.getQuestions() != null) {
                    for (Question question : quiz.getQuestions()) {
                        if (question.getImageBox() != null && !question.getImageBox().isEmpty()) {
                            System.out.println("question.getImageBox() :"  + question.getImageBox());
                            fileUtils.deleteFile(question.getImageBox());
                        }
                    }
                }
                quizService.deleteById(id);

                responseDTO.setStatusCode(HttpStatus.OK.value());
                return ResponseEntity.ok(responseDTO);
            } else {
                responseDTO.setErrorCode(500);
                responseDTO.setErrorMessage("이 퀴즈를 삭제할 권한이 없습니다.");
                responseDTO.setStatusCode(HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setErrorCode(404);
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

    @PostMapping("/completequiz")
    public ResponseEntity<?> completequiz(@RequestBody List<QuestionDTO> questionDTOList) {
        Map<Long, Boolean> answerResults = new HashMap<>();
        try {
            for (QuestionDTO questionDTO : questionDTOList) {
                Question question = questionService.findById(questionDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found for id: " + questionDTO.getId()));

                boolean isCorrect = Arrays.asList(question.getTanswer().split(",")).contains(questionDTO.getTanswer());
                answerResults.put(questionDTO.getId(), isCorrect);
            }
            return ResponseEntity.ok(answerResults);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", 103);
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
