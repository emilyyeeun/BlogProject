package me.yeeunhong.blogproject.service;

import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.AddArticleRequest;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogFactory {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        checkTitle(request.getTitle());
        checkContent(request.getContent());
        checkEmail(request.getEmail());
        checkPhoneNum(request.getPhoneNumber());
        checkPassword(request.getPassword());
        return blogRepository.save(request.toEntity());
    }

    private void checkTitle(String title) {
        // 1. title length 확인
        if (title.length() >= 200) {
            // title 이 글자 수 200 이상이 되면 안됨
            throw new IllegalArgumentException("title is longer than 200 characters");
        }
    }

    private void checkContent(String content) {
        // 2. content length 확인
        if (content.length() >= 1000) {
            // content 가 1000자 이상이 되어서는 안됨
            throw new IllegalArgumentException("content is longer than 1000 characters");
        }
    }

    private void checkEmail(String email) {
        final String emailFormat = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        //
        // 3. 이메일 형식 확인
        if (!isCorrectRegex(email, emailFormat)) {
            throw new IllegalArgumentException("wrong email format");
        }
    }

    private void checkPhoneNum(String phoneNumber) {
        final String phoneFormat = "\\d{3}-?\\d{4}-?\\d{4}";
        // 4. 휴대폰번호 형식 확인
        if (!isCorrectRegex(phoneNumber, phoneFormat)) {
            throw new IllegalArgumentException("wrong phone number format");
        }
    }

    private void checkPassword(String password) {
        // 5. 비밀번호 형식 확인
        // 대소문자로 이뤄져야하고 숫자가 5개이상 특수문자가 *!@#$%을 포함을 2개이상 해야한다.
        final String passwordFormat = "([a-zA-Z]+)([0-9]{5,})([*!@#$%]{2,})";
        if (!isCorrectRegex(password, passwordFormat)) {
            throw new IllegalArgumentException("wrong password format");
        }
    }

    private boolean isCorrectRegex(String input, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}

