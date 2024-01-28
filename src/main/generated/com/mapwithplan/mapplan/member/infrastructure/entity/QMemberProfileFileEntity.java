package com.mapwithplan.mapplan.member.infrastructure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberProfileFileEntity is a Querydsl query type for MemberProfileFileEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberProfileFileEntity extends EntityPathBase<MemberProfileFileEntity> {

    private static final long serialVersionUID = -1717937645L;

    public static final QMemberProfileFileEntity memberProfileFileEntity = new QMemberProfileFileEntity("memberProfileFileEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath storeFileName = createString("storeFileName");

    public final StringPath uploadMemberProfileFileName = createString("uploadMemberProfileFileName");

    public QMemberProfileFileEntity(String variable) {
        super(MemberProfileFileEntity.class, forVariable(variable));
    }

    public QMemberProfileFileEntity(Path<? extends MemberProfileFileEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberProfileFileEntity(PathMetadata metadata) {
        super(MemberProfileFileEntity.class, metadata);
    }

}

