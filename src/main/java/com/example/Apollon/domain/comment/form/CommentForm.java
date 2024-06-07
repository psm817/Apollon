package com.example.Apollon.domain.comment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @NotBlank(message="제목은 필수항목입니다.")
    @Size(max=100, message="제목을 100자 이하로 입력해주세요")
    private String title;

    @NotBlank(message="내용은 필수항목입니다.")
    @Size(max=1000, message="내용을 1,000자 이하로 입력해주세요")
    private String content;
}
