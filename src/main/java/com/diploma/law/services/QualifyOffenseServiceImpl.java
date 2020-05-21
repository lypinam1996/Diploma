package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

@Service
@Transactional
public class QualifyOffenseServiceImpl implements QualifyOffenseService
{

    @Autowired
    private ParseService                parseService;

    @Autowired
    private UserService                 userService;

    @Autowired
    private TaskService                 taskService;

    @Autowired
    private GetObjectService            getObjectService;

    @Autowired
    private GetSubjectService           getSubjectService;

    @Autowired
    private SearchOptionalThingsService searchOptionalThingsService;

    @Override
    public Model qualifyOffense(ProblemsEntity problem, Model result, BindingResult bindingResult)
    {
        if (!StringUtils.isEmpty(problem.getText()))
        {
            MainDto mainDto = parseService.parseText(problem.getText());
            if (mainDto.getObject() != null)
            {
                List<ArticlesEntity> articles = getObjectService.getObject(bindingResult, mainDto);
                UsersEntity user = userService.getUser();
                // getSubjectService.getVictimAndSubject(mainDto, problem);
                String date = searchOptionalThingsService.getDate(mainDto.getText());
                String address = searchOptionalThingsService.getAddress(mainDto.getText());
                String weapon = searchOptionalThingsService.getWeapon(mainDto.getText());
                getSubjectService.getSubject(mainDto, problem);

                problem.setArticle(articles);
                problem.setUsersByUser(user);
                problem.setText(problem.getText());
                problem.setDate(date);
                problem.setAddress(address);
                problem.setWeapon(weapon);

                result.addAttribute("articles", articles.get(0));
                result.addAttribute("subjects", problem.getSubject());
                result.addAttribute("victims", problem.getVictims());
                result.addAttribute("date", problem.getDate());
                result.addAttribute("address", problem.getAddress());
                result.addAttribute("weapon", problem.getWeapon());
            }
            taskService.saveTask(problem);
        }
        return result;
    }

}
