package com.revo.myboard.group;

import com.revo.myboard.exception.GroupNameInUseException;
import com.revo.myboard.exception.GroupNotExistsException;
import com.revo.myboard.group.dto.GroupDTO;
import com.revo.myboard.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

@Service
@Transactional
@AllArgsConstructor
public class GroupService {

    private static final String DEFAULT_GROUP_NAME = "UŻYTKOWNIK";

    private final GroupRepository repository;
    private final UserRepository userRepository;

    public Group getDefaultGroup() {
        if (repository.count() == 0 || repository.findByName(DEFAULT_GROUP_NAME).isEmpty()) {
            repository.save(buildDefaultGroup());
        }
        return findDefaultGroup();
    }

    private Group findDefaultGroup(){
        return repository.findByName(DEFAULT_GROUP_NAME).get();
    }

    private Group buildDefaultGroup(){
        return Group.builder().name(DEFAULT_GROUP_NAME).authority(Authority.USER).build();
    }

    public Group getGroupById(long id) {
        return repository.findById(id).orElseThrow(() -> new GroupNotExistsException(id));
    }

    GroupDTO getGroupDTOById(long id) {
        return mapFromGroup(getGroupById(id));
    }

    private GroupDTO mapFromGroup(Group group){
        return GroupMapper.mapFromGroup(group);
    }

    GroupDTO createGroup(String name, String authority) {
        if (existsByName(name)) {
            throw new GroupNameInUseException(name);
        }
        var group = repository.save(buildGroup(name, authority));
        return mapFromGroup(group);
    }

    private boolean existsByName(String name){
        return repository.existsByName(name);
    }

    private Group buildGroup(String name, String authority){
        return Group.builder().name(name).authority(Authority.valueOf(authority)).build();
    }

    void deleteGroupById(long id) {
        repository.delete(getGroupById(id));
        fixUsersGroup(id);
    }

    private void fixUsersGroup(long id){
        userRepository.findAll().stream().filter(user -> user.getGroup().getId() == id).forEach(user -> user.setGroup(getDefaultGroup()));
    }

    GroupDTO renameGroupById(long id, String new_name) {
        if (existsByName(new_name)) {
            throw new GroupNameInUseException(new_name);
        }
        var group = getGroupById(id);
        group.setName(new_name);
        return mapFromGroup(group);
    }

    public List<GroupDTO> getAllGroups() {
        return repository.findAll().stream().map(this::mapFromGroup).collect(Collectors.toList());
    }

    GroupDTO changeGroupAuthority(long id, String authority) {
        var group = getGroupById(id);
        group.setAuthority(Authority.valueOf(authority));
        return mapFromGroup(group);
    }

    List<String> getAuthorityList() {
        return Arrays.stream(Authority.values()).map(Object::toString).collect(Collectors.toList());
    }
}
