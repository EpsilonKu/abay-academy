package kz.bitter.project.services.impl;

import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Groups;
import kz.bitter.project.repositories.GroupRepository;
import kz.bitter.project.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Override
    public List<Groups> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Groups getGroupById(Long id) {
        return groupRepository.getOne(id);
    }

    @Override
    public Groups saveGroups(Groups groups) {
        return groupRepository.save(groups);
    }

    @Override
    public void removeGroups(Groups groups) {
        groupRepository.delete(groups);
    }

    @Override
    public Groups saveCourseToGroup(Groups group, Courses course) {
        group.getCourses().add(course);
        return groupRepository.save(group);
    }

    @Override
    public void kickCourseFromGroup(Groups group, Courses course) {
        group.getCourses().remove(course);
        groupRepository.save(group);
    }
}
