package com.tdl.tdl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PageInfo {

    int totalCnt; // 총 게시물 갯수
    int pageSize; // 한 페이지의 크기
    int naviSize = 10; // 페이지 내비게이션의 크기
    int totalPage; //전체 페이지의 개수
    int page; //현재 페이지
    int beginPage; //내비게이션의 첫번째 페이지
    int endPage; //내비게이션의 마지막 페이지
    boolean showPrev; //이전 페이지로 이동하는 링크를 보여줄것이지 여부
    boolean showNext; //다음 페이지로 이동하는 링크를 보여줄것이지 여부
    public PageInfo(int totalCnt, int page){
        this(totalCnt,page, 2);
    }

    public PageInfo(int totalCnt , int page , int pageSize){
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil(totalCnt/(double)pageSize);
        this.beginPage = (page-1) / naviSize * naviSize + 1;
        this.endPage = Math.min(beginPage + naviSize -1 , totalPage);
        this.showPrev = beginPage !=1;
        this.showNext = endPage != totalPage;
    }
}
