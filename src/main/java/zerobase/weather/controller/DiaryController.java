package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Operation(summary = "일기 등록")
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                     @Parameter(name = "날짜", example = "2024-05-06") LocalDate date,
            @RequestBody @Parameter(name = "내용", example = "일기 내용") String text) {
        diaryService.createDiary(date, text);
    }

    @Operation(summary = "특정 날짜의 일기 모두 조회")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                          @Parameter(name = "날짜", example = "2024-05-06") LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
            throw new InvalidDate();
        }
        return diaryService.readDiary(date);
    }

    @Operation(summary = "특정 기간 내의 일기 모두 조회")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @Parameter(name = "시작일", example = "2024-01-01") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @Parameter(name = "끝일", example = "2024-05-06") LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "특정 날짜의 일기 수정(가장 최근 등록한 일기만)")
    @PutMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                     @Parameter(name = "날짜", example = "2024-05-06") LocalDate date,
                     @RequestBody @Parameter(name = "내용", example = "일기 내용") String text) {
        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "특정 날짜의 일기 모두 삭제")
    @DeleteMapping("delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                     @Parameter(name = "날짜", example = "2024-05-06") LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
