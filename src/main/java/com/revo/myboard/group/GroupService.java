package com.revo.myboard.group;

import com.revo.myboard.exception.GroupNameInUseException;
import com.revo.myboard.exception.GroupNotExistsException;
import com.revo.myboard.group.dto.CreateDTO;
import com.revo.myboard.group.dto.GroupDTO;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
class GroupService implements GroupServiceApi{

    private static final String DEFAULT_GROUP_NAME = "UŻYTKOWNIK";

    private final GroupRepository repository;
    private final UserServiceApi userService;

    @Override
    public Group getDefaultGroup() {
        if (!existsByName(DEFAULT_GROUP_NAME)) {
            repository.save(buildDefaultGroup());
        }
        return findDefaultGroup();
    }

    private Group findDefaultGroup(){
        return repository.findByName(DEFAULT_GROUP_NAME)
                .orElseThrow(() -> new GroupNotExistsException(DEFAULT_GROUP_NAME));
    }

    private Group buildDefaultGroup(){
        return Group.builder()
                .name(DEFAULT_GROUP_NAME)
                .authority(Authority.USER)
                .build();
    }

    @Override
    public Group getGroupById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GroupNotExistsException(id));
    }

    GroupDTO getGroupDTOById(long id) {
        return mapFromGroup(getGroupById(id));
    }

    private GroupDTO mapFromGroup(Group group){
        return GroupMapper.mapFromGroup(group);
    }

    GroupDTO createGroup(CreateDTO createDTO) {
        var name = createDTO.getName();
        if (existsByName(name)) {
            throw new GroupNameInUseException(name);
        }
        var authority = createDTO.getAuthority();
        var group = repository.save(buildGroup(name, authority));
        return mapFromGroup(group);
    }

    private boolean existsByName(String name){
        return repository.existsByName(name);
    }

    private Group buildGroup(String name, String authority){
        return Group.builder()
                .name(name)
                .authority(Authority.valueOf(authority))
                .build();
    }

    void deleteGroupById(long id) {
        repository.delete(getGroupById(id));
        fixUsersGroup(id);
    }

    private void fixUsersGroup(long id){
        userService.getAll().stream()
                .filter(user -> getIdOfGroup(user.getGroup()) == id)
                .forEach(user -> user.setGroup(getDefaultGroup()));
    }

    private long getIdOfGroup(Group group) {
        return group.getId();
    }

    GroupDTO renameGroupById(long id, String new_name) {
        if (existsByName(new_name)) {
            throw new GroupNameInUseException(new_name);
        }
        var group = getGroupById(id);
        group.setName(new_name);
        return mapFromGroup(group);
    }

    List<GroupDTO> getAllGroups() {
        var groups = getAll();
        return mapFromList(groups);
    }

    private List<GroupDTO> mapFromList(List<Group> all) {
        return all.stream()
                .map(this::mapFromGroup)
                .toList();
    }

    private List<Group> getAll() {
        return repository.findAll();
    }

    GroupDTO changeGroupAuthority(long id, String authority) {
        var group = getGroupById(id);
        group.setAuthority(Authority.valueOf(authority));
        return mapFromGroup(group);
    }

    List<String> getAuthorityList() {
        return mapFromListAsString(Authority.values());
    }

    private List<String> mapFromListAsString(Authority[] values) {
        return Arrays.stream(values)
                .map(Object::toString)
                .toList();
    }
}
