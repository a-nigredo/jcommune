/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.plugin.api.web.dto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.jtalks.jcommune.model.entity.Poll;
import org.jtalks.jcommune.model.entity.PollItem;
import org.jtalks.jcommune.model.entity.Post;
import org.jtalks.jcommune.model.entity.Topic;
import org.jtalks.jcommune.plugin.api.web.validation.annotations.BbCodeAwareSize;
import org.jtalks.jcommune.plugin.api.web.validation.annotations.BbCodeNesting;
import org.springframework.util.AutoPopulatingList;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link Topic} objects. Used for validation and binding to form.
 *-
 * @author Vitaliy Kravchenko
 * @author Max Malakhov
 * @author Eugeny Batov
 */
public class TopicDto {

    private Topic topic;

    @BbCodeAwareSize(min = Post.MIN_LENGTH, max = Post.MAX_LENGTH)
    @BbCodeNesting
    private String bodyText;

    private String topicUrl;
    private String unreadIconUrl;
    private String readIconUrl;
    private String postUrlPrefix;
    /**
     * Used for UI only
     */
    private List<PollItem> pollItemsValue = new AutoPopulatingList(PollItem.class);
    /**
     * Contains not empty PollItem
     */
    private List<PollItem> pollItems = new ArrayList<>();

    /**
     * Plain object for topic creation
     */
    public TopicDto() {
    }

    /**
     * Create dto from {@link Topic}
     *
     * @param topic topic for conversion
     */
    public TopicDto(Topic topic) {
        this.topic = topic;
        if (!topic.getPosts().isEmpty()) {
            bodyText = topic.getBodyText();
        }
    }

    /**
     * @return topic that used as dto between controllers and services
     */
    @Valid
    public Topic getTopic() {
        copyPollItem();
        return topic;
    }

    /**
     * Copy PollItem from UI collection to Poll. Need that for correct Poll validation.
     */
    private void copyPollItem() {
        CollectionUtils.select(pollItemsValue, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                PollItem pollItem = (PollItem) object;
                return StringUtils.isNotEmpty(pollItem.getName()) && !pollItems.contains(pollItem);
            }
        }, pollItems);
    }

    /**
     * Set topic in dto. Used in tests.
     *
     * @param topic topic
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * Get first post content.
     *
     * @return first post content
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * Set first post content.
     *
     * @param bodyText content of first post in topic
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    /**
     * @return poll in topic
     */
    public Poll getPoll() {
        return topic.getPoll();
    }

    /**
     * Set poll in topic.
     *
     * @param poll poll
     */
    public void setPoll(Poll poll) {
        topic.setPoll(poll);
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getUnreadIconUrl() {
        return unreadIconUrl;
    }

    public void setUnreadIconUrl(String unreadIconUrl) {
        this.unreadIconUrl = unreadIconUrl;
    }

    public String getReadIconUrl() {
        return readIconUrl;
    }

    public void setReadIconUrl(String readIconUrl) {
        this.readIconUrl = readIconUrl;
    }

    /**
     * Get the prefix for url for one of posts of the topic
     * @return the prefix for url for one of posts of the topic
     */
    public String getPostUrlPrefix() {
        return postUrlPrefix;
    }

    /**
     * Sets the prefix for url for one of posts of the topic
     * @param postUrlPrefix new prefix for url for one of posts of the topic
     */
    public void setPostUrlPrefix(String postUrlPrefix) {
        this.postUrlPrefix = postUrlPrefix;
    }

    /**
     * Fills persistent topic object with data from the current dto
     *
     * @param persistentTopic persistent topic
     * @return the same topic with fields set from dto
     */
    public Topic fillTopic(Topic persistentTopic) {
        persistentTopic.setTitle(topic.getTitle());
        persistentTopic.getFirstPost().setPostContent(bodyText);
        persistentTopic.setAnnouncement(topic.isAnnouncement());
        persistentTopic.setSticked(topic.isSticked());
        return topic;
    }

    /**
     * @return poll options in string representation.
     */
    public List<PollItem> getPollItemsValue() {
        topic.getPoll().setPollItems(pollItems);
        return pollItemsValue;
    }
}