package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.CurrentHeistMemberDTO;
import com.hackatonapi.HackatonRest.DTO.MemberDTO;
import com.hackatonapi.HackatonRest.entity.Member;

public interface MemberMapper {
    MemberDTO memberToMemberDTO(Member member);
    CurrentHeistMemberDTO memberToCurrentHeistMemberDTO(Member member);

}
