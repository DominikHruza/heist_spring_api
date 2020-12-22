package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.*;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.HeistTimestampException;
import com.hackatonapi.HackatonRest.exception.InvalidHeistStatusException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.mappers.HeistMapper;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.repository.RequiredSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeistServiceImpl implements HeistService {

    HeistRepository heistRepository;
    HeistMapper heistMapper;
    MemberMapper memberMapper;
    MemberRepository memberRepository;

    @Autowired
    public HeistServiceImpl(HeistRepository heistRepository,
                            HeistMapper heistMapper,
                            MemberMapper memberMapper,
                            MemberRepository memberRepository) {
        this.heistRepository = heistRepository;
        this.heistMapper = heistMapper;
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    @Override
    public HeistDTO getHeist(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + id + " does not exist."
            );
        }

        Heist heist = heistOptional.get();

        return heistMapper.heistToHeistDTO(heist);
    }

    @Override
    @Transactional
    public HeistDTO saveNewHeist(HeistDTO heistDTO) {
        checkTimestampValidity(heistDTO.getStartTime(), heistDTO.getEndTime());

        Heist newHeist = new Heist(
                heistDTO.getName(),
                heistDTO.getLocation(),
                heistDTO.getStartTime(),
                heistDTO.getEndTime(),
                HeistStatus.PLANNING
        );

        try {
            heistRepository.save(newHeist);
        } catch (Exception e){
            throw new DuplicateResourceEntryException(
                    "Member with name " + heistDTO.getName() + " already exists");
        }

        HeistDTO newHeistDTO = heistMapper.heistToHeistDTO(newHeist);

        return newHeistDTO;
    }

    @Override
    @Transactional
    public void startHeist(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + heistId + " does not exist."
            );
        }

        Heist heist = heistOptional.get();

        if (heist.getStatus() != HeistStatus.READY){
            throw new InvalidHeistStatusException(
                    "Can not start heist. Heist is not in status READY."
            );
        }

        heist.setStatus(HeistStatus.IN_PROGRESS);
    }

    @Override
    public List<CurrentHeistMemberDTO> getHeistMembers(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + heistId + " does not exist."
            );
        }

        Heist heist = heistOptional.get();
        if(heist.getStatus().equals(HeistStatus.PLANNING)){
            throw new InvalidHeistStatusException(
                    "Can not display heist data. " +
                    "Heist is in status" + heist.getStatus()
            );
        }

        List<CurrentHeistMemberDTO> currentMembersDTO = heist
                .getMembers()
                .stream()
                .map(member ->
                        memberMapper.memberToCurrentHeistMemberDTO(member))
                .collect(Collectors.toList());

        return currentMembersDTO;
     }

    @Override
    public HeistStatusDTO getHeistStatus(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + heistId + " does not exist."
            );
        }

        Heist heist = heistOptional.get();
        return new HeistStatusDTO(heist.getStatus().name());
    }

    @Override
    @Transactional
    public void changeStatus(Long id, HeistStatus status) {
        Optional<Heist> heistOptional = heistRepository.findById(id);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + id + " does not exist."
            );
        }

        Heist heist = heistOptional.get();
        heist.setStatus(status);
    }

    @Override
    @Transactional
    public void addMemberToHeist(Long id, String memberName) {
        Optional<Member> memberOptional = memberRepository.findByName(memberName);
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if(!memberOptional.isPresent()){
            throw new ResourceNotFoundException("Member with name " + memberName + " not found");
        }

        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException("Heist with id " + id + " not found");
        }

        Member member = memberOptional.get();
        Heist heist = heistOptional.get();

        member.addToHeists(heist);
        heist.addToMembers(member);
    }

    @Override
    public Double calculateMembersPercentage(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + heistId + " does not exist."
            );
        }
        Heist heist = heistOptional.get();
        Integer requiredMembers = calculateRequiredMembers(heist);
        Double reqMembersPercentage = (double) heist.getMembers().size() / requiredMembers;

        return reqMembersPercentage;
    }

    @Override
    @Transactional
    public HeistOutcome setHeistOutcome(Long id, HeistOutcome outcome) {
        Optional<Heist> heistOptional = heistRepository.findById(id);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + id + " does not exist."
            );
        }
        Heist heist = heistOptional.get();
        heist.setOutcome(outcome);
        return heist.getOutcome();
    }

    @Override
    @Transactional
    public HeistOutcome setHeistOutcome(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + id + " does not exist."
            );
        }
        Heist heist = heistOptional.get();
        HeistOutcome randOutcome = HeistOutcome.getRandomOutcome();
        heist.setOutcome(randOutcome);
        return randOutcome;
    }

    private boolean checkTimestampValidity(ZonedDateTime start, ZonedDateTime end){
        if(start.isAfter(end)){
           throw new HeistTimestampException("Heist start time is after end time.");
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));
        if(now.isAfter(end)){
            throw new HeistTimestampException("Heist end time is before current time.");
        }

        return true;
    }

    private Integer calculateRequiredMembers(Heist heist){
        List<RequiredSkill> requiredSkillList = heist.getRequiredSkillList();
        Integer membersSum = 0;
        for (RequiredSkill skill : requiredSkillList) {
            membersSum += skill.getMembers();
        }

        return membersSum;
    }
}
