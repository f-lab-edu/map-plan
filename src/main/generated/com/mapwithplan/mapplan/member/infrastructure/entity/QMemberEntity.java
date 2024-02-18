package com.mapwithplan.mapplan.member.infrastructure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberEntity is a Querydsl query type for MemberEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberEntity extends EntityPathBase<MemberEntity> {

    private static final long serialVersionUID = 498131384L;

    public static final QMemberEntity memberEntity = new QMemberEntity("memberEntity");

    public final com.mapwithplan.mapplan.common.timeutils.entity.QBaseTimeEntity _super = new com.mapwithplan.mapplan.common.timeutils.entity.QBaseTimeEntity(this);

    public final StringPath certificationCode = createString("certificationCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.mapwithplan.mapplan.member.domain.EMemberRole> memberRole = createEnum("memberRole", com.mapwithplan.mapplan.member.domain.EMemberRole.class);

    public final EnumPath<com.mapwithplan.mapplan.member.domain.EMemberStatus> memberStatus = createEnum("memberStatus", com.mapwithplan.mapplan.member.domain.EMemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath statusMessage = createString("statusMessage");

    public QMemberEntity(String variable) {
        super(MemberEntity.class, forVariable(variable));
    }

    public QMemberEntity(Path<? extends MemberEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberEntity(PathMetadata metadata) {
        super(MemberEntity.class, metadata);
    }

}

