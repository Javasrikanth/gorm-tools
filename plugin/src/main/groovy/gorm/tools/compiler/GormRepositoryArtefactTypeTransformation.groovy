package gorm.tools.compiler

import grails.plugin.gormtools.RepositoryArtefactHandler
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.grails.compiler.injection.ArtefactTypeAstTransformation

/**
 * A transformation that makes an Artefact a GormRepository
 */

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class GormRepositoryArtefactTypeTransformation extends ArtefactTypeAstTransformation {

    @Override
    protected String resolveArtefactType(SourceUnit sourceUnit, AnnotationNode annotationNode, ClassNode classNode) {
        return RepositoryArtefactHandler.TYPE
    }

    @Override
    protected Class getAnnotationTypeClass() {
        return GormRepository.class
    }
}
