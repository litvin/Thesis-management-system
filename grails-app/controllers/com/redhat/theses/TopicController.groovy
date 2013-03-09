package com.redhat.theses

import com.redhat.theses.util.Util

class TopicController {

    /**
     * Dependency injection of com.redhat.theses.TopicService
     */
    def topicService;

    /**
     * Dependency injection of grails.plugins.springsecurity.SpringSecurityService
     */
    def springSecurityService

    /**
     * Dependency injection of com.redhat.theses.CommentService
     */
    def commentService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params, permanent: true)
    }

    def list(Integer max) {
        params.max = Util.max(max)
        def categoryList = Category.findAll()
        def topics = Topic.list(params)
        def commentCounts = commentService.countByArticles(topics)

        [topicInstanceList: topics, topicInstanceTotal: Topic.count(),
                commentCounts: commentCounts, categoryList: categoryList]
    }

    def category(Long id, Integer max) {
        if (!id){
            redirect(action: "list")
            return
        }

        params.max = Util.max(max)
        def category = Category.get(id)

        if (!category) {
            flash.message = message(code: 'category.not.found', args: [id])
            redirect(action: "list")
            return
        }

        def topicInstanceList = Topic.findAllByCategory(category, params)
        def topicInstanceTotal = Topic.countByCategory(category)

        def commentCounts = commentService.countByArticles(topicInstanceList)

        [topicInstanceList: topicInstanceList, topicInstanceTotal: topicInstanceTotal,
                currentCategory: category, commentCounts: commentCounts]
    }

    def create(Long categoryId) {
        def supervisionCommand = new SupervisionCommand()
        bindData(supervisionCommand, params.supervisionCommand)

        def topicInstance = new Topic(params.topic)
        // if category id is set, set default category to the current category
        if (categoryId) {
            def categoryInstance = Category.get(categoryId)
            if (categoryInstance) {
                topicInstance.categories = [categoryInstance]
            }
        }

        // set default types to Bachelor and master
        topicInstance.types = [Type.BACHELOR, Type.MASTER]

        [topicInstance: topicInstance, supervisionCommand: supervisionCommand,
                universities: University.all, types: Type.values()]
    }

    def save() {
        def topicInstance = new Topic(params.topic)
        def supervisionCommand = new SupervisionCommand()
        bindData(supervisionCommand, params.supervisionCommand)

        if (!topicService.saveWithSupervisions(topicInstance, supervisionCommand.supervisions)) {
            render(view: "create", model: [topicInstance: topicInstance, supervisionCommand: supervisionCommand,
                    universities: University.all, types: Type.values()])
            return
        }

        flash.message = message(code: 'topic.created', args: [topicInstance.id])
        redirect(action: "show", id: topicInstance.id, params: [headline: Util.hyphenize(topicInstance.title)])
    }

    def show(Long id, String headline) {
        def topicInstance = Topic.get(id)
        if (!topicInstance) {
            flash.message = message(code: 'topic.not.found', args: [id])
            redirect(action: "list")
            return
        }

        if (Util.hyphenize(topicInstance.title) != headline) {
            redirect(action: 'show', id: id, params: [headline: Util.hyphenize(topicInstance.title)], permanent: true)
        }

        def supervisions = topicInstance.supervisions

        def commentsTotal = Comment.countByArticle(topicInstance)
        def defaultOffset = Util.lastOffset(commentsTotal, params.max, params.offset)

        def comments = Comment.findAllByArticle(topicInstance,
                [max: Util.max(params.max), sort: 'dateCreated', offset: defaultOffset])

        def subscriber = Subscription.findBySubscriberAndArticle(springSecurityService.currentUser, topicInstance)

        [topicInstance: topicInstance, supervisions: supervisions,
                comments: comments, commentsTotal: commentsTotal, subscriber: subscriber]
    }

    def edit(Long id, SupervisionCommand supervisionCommand) {
        def topicInstance = Topic.get(id)
        if (!topicInstance) {
            flash.message = message(code: 'topic.not.found', args: [id])
            redirect(action: "list")
            return
        }

        supervisionCommand.supervisions += topicInstance.supervisions

        [topicInstance: topicInstance, supervisionCommand: supervisionCommand,
                universities: University.all, types: Type.values()]
    }

    def update() {
        Long id = params.topic.long("id")
        Long version = params.topic.long("version")
        def topicInstance = Topic.get(id)
        if (!topicInstance) {
            flash.message = message(code: 'topic.not.found', args: [id])
            redirect(action: "list")
            return
        }

        topicInstance.properties = params.topic
        // when no categories selected, set them to empty list
        if (!params.topic.categories) {
            topicInstance.categories = []
        }

        def supervisionCommand = new SupervisionCommand()
        bindData(supervisionCommand, params.supervisionCommand)
        supervisionCommand.supervisions = supervisionCommand.supervisions.findAll()

        if (version != null && topicInstance.version > version) {
            topicInstance.errors.rejectValue("version", "topic.optimistic.lock.error")
            render(view: "edit", model: [topicInstance: topicInstance, supervisionCommand: supervisionCommand,
                    universities: University.all, types: Type.values()])
            return
        }

        //TODO: refactoring?
        def withExistingSupervisions = supervisionCommand.supervisions.collect {
            def supervision = Supervision.findByTopicAndSupervisorAndUniversity(topicInstance, it.supervisor, it.university)
            if (supervision) {
                supervision
            } else {
                it
            }
        }
        if (!topicService.saveWithSupervisions(topicInstance, withExistingSupervisions))  {
            render(view: "edit", model: [topicInstance: topicInstance, supervisionCommand: supervisionCommand,
                    universities: University.all, types: Type.values()])
            return
        }

        flash.message = message(code: 'topic.updated', args: [topicInstance.id])
        redirect(action: "show", id: topicInstance.id, params: [headline: Util.hyphenize(topicInstance.title)])
    }

    def delete() {
        Long id = params.topic.long("id")
        def topicInstance = Topic.get(id)
        if (!topicInstance) {
            flash.message = message(code: 'topic.not.found', args: [id])
            redirect(action: "list")
            return
        }

        if (topicService.deleteWithSupervisions(topicInstance)) {
            flash.message = message(code: 'topic.deleted', args: [id])
            redirect(action: "list")
        } else {
            flash.message = message(code: 'topic.not.deleted', args: [id])
            redirect(action: "show", id: id, params: [headline: Util.hyphenize(topicInstance.title)])
        }
    }
}
